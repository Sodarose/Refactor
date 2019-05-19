package ulits;

import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.type.Type;
import io.FileUlits;
import io.ParserProject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kangkang
 */
public class FindNodeUlits {

    /**
     * 寻找参数类型说明Node
     */
    public static List<VariableDeclarationExpr> findVariableDeclarationExprByName(Node node, String name,
                                                                                  boolean isRecursive) {
        BaseVisitor<VariableDeclarationExpr> visitor = new BaseVisitor<VariableDeclarationExpr>() {
            @Override
            public void visit(VariableDeclarationExpr n, Object arg) {
                List<VariableDeclarator> list = n.getVariables();
                if (name != null && !name.equals("")) {
                    for (VariableDeclarator variable : list) {
                        if (variable.getName().getIdentifier().equals(name)) {
                            getList().add(n);
                            break;
                        }
                    }
                } else {
                    getList().add(n);
                }

                if (isRecursive) {
                    super.visit(n, arg);
                }
            }
        };
        node.accept(visitor, null);
        return visitor.getList();
    }

    /**
     * 寻找参数类型说明Node
     */
    public static List<UnaryExpr> findUnaryExprByName(Node node, String name, boolean isRecursive) {
        BaseVisitor<UnaryExpr> visitor = new BaseVisitor<UnaryExpr>() {
            @Override
            public void visit(UnaryExpr n, Object arg) {
                if (n.getExpression().isNameExpr()) {
                    if (n.getExpression().asNameExpr().getName().getIdentifier().equals(name)) {
                        getList().add(n);
                    }
                }
                if (isRecursive) {
                    super.visit(n, arg);
                }
            }
        };
        node.accept(visitor, null);
        return visitor.getList();
    }


    /**
     * simpleName
     */
    public static List<SimpleName> findSimpleNameByName(Node node, String name, boolean isRecursive) {
        BaseVisitor<SimpleName> visitor = new BaseVisitor<SimpleName>() {
            @Override
            public void visit(SimpleName n, Object arg) {
                if (name != null && !name.equals("")) {
                    if (n.getIdentifier().equals(name)) {
                        getList().add(n);
                    }
                } else {
                    getList().add(n);
                }
                if (isRecursive) {
                    super.visit(n, arg);
                }
            }
        };
        node.accept(visitor, null);
        return visitor.getList();
    }


    /**
     * IfStmt
     */
    public static List<IfStmt> findIfStmtByName(Node node, String name, boolean isRecursive) {
        BaseVisitor<IfStmt> visitor = new BaseVisitor<IfStmt>() {
            @Override
            public void visit(IfStmt n, Object arg) {
                getList().add(n);
                if (isRecursive) {
                    super.visit(n, arg);
                }
            }
        };
        node.accept(visitor, null);
        return visitor.getList();
    }

    /**
     * 类属性声明
     * find FieldDeclaration
     */
    public static List<FieldDeclaration> findFieldDeclarationByName(Node node, String name, boolean isRecursive) {
        BaseVisitor<FieldDeclaration> visitor = new BaseVisitor<FieldDeclaration>() {
            @Override
            public void visit(FieldDeclaration n, Object arg) {
                if (name != null && !name.equals("")) {
                    for (VariableDeclarator variable : n.getVariables()) {
                        if (variable.getName().getIdentifier().equals(name)) {
                            getList().add(n);
                            break;
                        }
                    }
                } else {
                    getList().add(n);
                }
                if (isRecursive) {
                    super.visit(n, arg);
                }
            }
        };
        node.accept(visitor, null);
        return visitor.getList();
    }

    /**
     * 从局部变量找
     */
    public static Map<String, Node> findTypeByLocal(String name, Node node) {
        Node temp = node;
        Map<String, Node> map = new HashMap<>(1);
        /**
         * 找到最接近unit的父类
         * */
        while(temp.getParentNode().isPresent()){
            if(temp.getParentNode().get().getClass().getName().equals("com.github.javaparser.ast.CompilationUnit")){
                break;
            }
            temp = temp.getParentNode().get();
        }
        List<VariableDeclarationExpr> localVar = temp.getParentNode().get().findAll(VariableDeclarationExpr.class);
        for (VariableDeclarationExpr expr : localVar) {
            for (VariableDeclarator variable : expr.getVariables()) {
                if (variable.getName().getIdentifier().equals(name)) {
                    map.put(variable.getType().asString(), variable);
                    System.out.println(variable.getType());
                }
            }
        }
        return map;
    }

    public static Map<String, Node> findTypeByClass(String name, Node node) {
        Node temp = node;
        Map<String, Node> map = new HashMap<>(1);
        //全局声明变量 其次
        String fullName = node.findRootNode().getClass().getName();
        if (!"com.github.javaparser.ast.CompilationUnit".equals(fullName)) {
            return null;
        }
        List<FieldDeclaration> classVar = node.findRootNode().findAll(FieldDeclaration.class);
        for (FieldDeclaration field : classVar) {
            for (VariableDeclarator variable : field.getVariables()) {
                if (variable.getName().getIdentifier().equals(name)) {
                    map.put(variable.getType().asString(), variable);
                    System.out.println(variable.getType());
                }
            }
        }
        return map;
    }

    public static Map<String, Node> findTypeByAllClass(String name, Node node){
        Map<String, Node> map = new HashMap<>(1);
        String fullName = node.findRootNode().getClass().getName();
        if (!"com.github.javaparser.ast.CompilationUnit".equals(fullName)) {
            return null;
        }
        //最终项目变量查找
        List<CompilationUnit> units = ParserProject.parserProject("D:\\gitProject\\W8X\\core\\src\\test\\java");
        CompilationUnit root = (CompilationUnit) node.findRootNode();
        List<ImportDeclaration> importDeclarations = root.getImports();
        String className = name;
        //通过导入的包获得完整
        for (ImportDeclaration importDeclaration : importDeclarations) {
            String fn = importDeclaration.getName().asString();
            String n = fn.substring(fn.lastIndexOf(".") + 1);
            if (n.equals(name)) {
                className = fn;
            }
        }
        for (CompilationUnit unit : units) {
            String pageName = "";
            if (!unit.getPackageDeclaration().isPresent()) {
                pageName = "";
            } else {
                pageName = unit.getPackageDeclaration().get().getName().asString();
            }
            List<ClassOrInterfaceDeclaration> data = unit.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration declaration : data) {
                //完整类名是否匹配
                if ((pageName + declaration.getName().getIdentifier()).equals(className)) {
                    map.put(className,unit);
                    System.out.println(className);
                }
            }
        }
        return map;
    }


    public static void main(String[] args) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\ForWhileSampe.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        findUnaryExprByName(unit, null, true);
    }
}
