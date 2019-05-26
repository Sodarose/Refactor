package io;

import com.alibaba.fastjson.JSON;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CollectionStrategy;
import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import model.NodeTree;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.security.krb5.internal.PAData;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

/**
 * read all java file by one project
 */

public class ParserProject {

    private static List<CompilationUnit> javaFiles;
    private static ProjectRoot projectRoot;
    private static JavaParserFacade javaParserFacade;
    private static CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
    private static Map<String,CompilationUnit> unitMaps = new HashMap<>();

    public static List<CompilationUnit> parserProject(String path) {
        loadProject(path);
        if (projectRoot == null) {
            throw new RuntimeException("读取项目失败");
        }
        if (projectRoot.getSourceRoots() == null || projectRoot.getSourceRoots().size() == 0) {
            throw new RuntimeException("读取项目失败");
        }
        TypeSolver reflectionTypeSolver = new ReflectionTypeSolver();

        combinedTypeSolver.add(reflectionTypeSolver);
        List<CompilationUnit> units = new ArrayList<>();
        try {
            List<SourceRoot> sourceRoots = projectRoot.getSourceRoots();
            Iterator<SourceRoot> itRoots = sourceRoots.iterator();
            while (itRoots.hasNext()) {
                SourceRoot sourceRoot = itRoots.next();
                combinedTypeSolver.add(new JavaParserTypeSolver(sourceRoot.getRoot()));
                List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
                Iterator<ParseResult<CompilationUnit>> it = parseResults.iterator();
                while (it.hasNext()) {
                    ParseResult<CompilationUnit> parseResult = it.next();
                    if (!parseResult.getResult().isPresent()) {
                        continue;
                    }
                    units.add(parseResult.getResult().get());
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        javaFiles = units;
        return units;
    }

    public static List<CompilationUnit> getJavaFiles() {
        return javaFiles;
    }

    private static void loadProject(String path) {
        CollectionStrategy collectionStrategy = new ParserCollectionStrategy();
        projectRoot = collectionStrategy.collect(Paths.get(path));
    }

    public static JavaParserFacade getJavaParserFacade() {
        javaParserFacade = JavaParserFacade.get(combinedTypeSolver);
        return javaParserFacade;
    }

    public static CombinedTypeSolver getCombinedTypeSolver() {
        return combinedTypeSolver;
    }

    public static void saveAll() {
        for (SourceRoot sourceRoot : projectRoot.getSourceRoots()) {
            sourceRoot.saveAll();
        }
    }

    public static NodeTree getNodeTree(){
        parserProject("D:\\gitProject\\W8X");
        projectRoot.getSourceRoots();
        String rootName = projectRoot.getRoot().toFile().getName();
        NodeTree root = new NodeTree();
        root.setFileName(rootName);
        for(SourceRoot sourceRoot:projectRoot.getSourceRoots()){
            NodeTree sourceTree = new NodeTree();
            String path = sourceRoot.getRoot().toFile().getPath();
            Map<PackageDeclaration,List<NodeTree>> map = new HashMap<>(1);
            if(path.contains(rootName)){
                path = path.substring(path.indexOf(rootName)+rootName.length()+1);
            }
            sourceTree.setFileName(path);
            root.getFiles().add(sourceTree);
            for(CompilationUnit unit:sourceRoot.getCompilationUnits()){
                PackageDeclaration packageNmae =null;
                if(unit.getPackageDeclaration().isPresent()){
                    packageNmae = unit.getPackageDeclaration().get();
                }
                NodeTree node = new NodeTree();
                node.setFileName(unit.getPrimaryTypeName().get());
                if(map.containsKey(packageNmae)){
                    map.get(packageNmae).add(node);
                }else{
                    List<NodeTree> list = new ArrayList<>();
                    list.add(node);
                    map.put(packageNmae,list);
                }
            }
            for(Map.Entry<PackageDeclaration,List<NodeTree>> entry:map.entrySet()){
                if(entry.getKey()==null){
                    sourceTree.getFiles().addAll(entry.getValue());
                    continue;
                }
                NodeTree t = new NodeTree();
                t.setFileName(entry.getKey().toString());
                t.getFiles().addAll(entry.getValue());
                sourceTree.getFiles().add(t);
            }

        }
        System.out.println(JSON.toJSONString(root));
        return null;
    }

    private static String getPath(Path rootPath, Path sourceRoot) {
        List<String> names = new ArrayList();
        while(sourceRoot.getParent()!=rootPath){
            names.add(sourceRoot.toFile().getName());
            sourceRoot = sourceRoot.getParent();
        }
        Collections.reverse(names);
        StringBuffer name = new StringBuffer();
        for(String n:names){
            name.append(n);
        }
        return name.toString();
    }

    public static void main(String args[]) throws IOException{
        getNodeTree();
    }


}
