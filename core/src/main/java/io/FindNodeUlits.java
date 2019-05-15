package io;


import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import io.FileUlits;

import java.util.List;

/**
 * 快速查找
 * */
public class FindNodeUlits {
        public static List<SwitchStmt> getSwitch(String path){
            String source = FileUlits.readFile(path);
            CompilationUnit unit = StaticJavaParser.parse(source);
            BaseVisitor<SwitchStmt> visitor = new BaseVisitor<SwitchStmt>() {
                @Override
                public void visit(SwitchStmt n, Object arg) {
                   getList().add(n);
                }
            };
            unit.accept(visitor, null);
            return visitor.getList();
        }
    public static List<ForStmt> getFor(String path){
        String source = FileUlits.readFile(path);
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<ForStmt> visitor = new BaseVisitor<ForStmt>() {
            @Override
            public void visit(ForStmt n, Object arg) {
                getList().add(n);
                super.visit(n,arg);
            }
        };
        unit.accept(visitor, null);
        return visitor.getList();
    }
}
