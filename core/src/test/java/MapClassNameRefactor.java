import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import io.FileUlits;
import ulits.BoyerMoore;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapClassNameRefactor {
    private  Pattern pattern=Pattern.compile("[a-zA-Z]*Map{1}<[A-Za-z0-9]*,\\s?[A-Za-z0-9]*>");
    public static void main(String args[]) {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\Importtest\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<VariableDeclarationExpr> variableDeclarationExprs = unit.findAll(VariableDeclarationExpr.class);
        for (VariableDeclarationExpr var : variableDeclarationExprs) {
            for(VariableDeclarator node:var.getVariables()) {
                String name = node.getType().getClass().getName();
                if(name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                    String className = node.getType().toString();
                    MapClassNameRefactor mapClassNameRefactor = new MapClassNameRefactor();
                    Matcher flag = mapClassNameRefactor.judgeMap(className);
                    if (flag.find()) {
                        ClassOrInterfaceType type = (ClassOrInterfaceType) node.getType();
                        NodeList<Type> typeList = type.getTypeArguments().get();
                        NodeList<Type> resultList=new NodeList<>();
                        for (Type type1 : typeList) {
                            String typeName = type1.getClass().getName();
                            if (name.equals("com.github.javaparser.ast.type.ClassOrInterfaceType")) {
                                ClassOrInterfaceType type2 = (ClassOrInterfaceType) type1;
                                if (type2.getNameAsString().equals("ClassVariRefactor")) {
                                    type2.setName("classVariRefactor");
                                    resultList.add(type2);
                                    continue;
                                }
                            }
                            resultList.add(type1);
                        }
                        type.setTypeArguments(resultList);
                        /*Optional<Expression> initializer = node.getInitializer();
                        if (initializer != null && initializer.isPresent()) {
                            if (initializer.get().getClass().toString().equals("class com.github.javaparser.ast.expr.ObjectCreationExpr")) {
                                ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) initializer.get();
                                ClassOrInterfaceType type2 = (ClassOrInterfaceType) objectCreationExpr.getType().getTypeArguments().get().get(0);
                                type2.setName("classVariRefactor");
                                objectCreationExpr.setTypeArguments(type2);
                                objectCreationExpr.removeTypeArguments();
                            }
                        }*/
                        }
                    }
                }}
        }
    public Matcher judgeMap(String name){
        Matcher m =pattern.matcher(name);
        return m;
    }
}
