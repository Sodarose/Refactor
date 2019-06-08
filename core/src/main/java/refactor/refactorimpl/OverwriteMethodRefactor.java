package refactor.refactorimpl;

import analysis.rule.OverwriteMethodRule;
import api.AnalysisApi;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import io.FileUlits;
import model.Issue;
import model.JavaModel;
import refactor.AbstractRefactor;

import java.util.ArrayList;
import java.util.List;

public class OverwriteMethodRefactor extends AbstractRefactor {
    @Override
    public void refactor(Issue issue) {
        MethodDeclaration method = (MethodDeclaration) issue.getIssueNode();
        fix(method);

    }

    private void fix(MethodDeclaration method) {
        MarkerAnnotationExpr markerAnnotationExpr = new MarkerAnnotationExpr();
        markerAnnotationExpr.setName("Override");
        method.getAnnotations().add(markerAnnotationExpr);
    }


}
