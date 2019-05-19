package io;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
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
import sun.security.krb5.internal.PAData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * read all java file by one project
 */

public class ParserProject {

    private static List<CompilationUnit> javaFiles;
    private static ProjectRoot projectRoot;
    private static JavaParserFacade javaParserFacade;
    private static CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();

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

    public static JavaParserFacade getJavaParserFacade(){
        javaParserFacade  = JavaParserFacade.get(combinedTypeSolver);
        return javaParserFacade;
    }

    public static CombinedTypeSolver getCombinedTypeSolver(){
        return combinedTypeSolver;
    }





    public static void main(String agrs[]){
        parserProject("D:\\gitProject\\W8X");
        //查找某个类 必须是全名
        ResolvedReferenceTypeDeclaration type = combinedTypeSolver.solveType("PPX");
        System.out.println(type);
    }
}
