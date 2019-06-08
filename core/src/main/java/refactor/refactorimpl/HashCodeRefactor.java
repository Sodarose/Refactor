package refactor.refactorimpl;

import analysis.rule.HashCodeRule;
import api.AnalysisApi;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import io.FileUlits;
import model.Issue;
import model.JavaModel;
import refactor.AbstractRefactor;

import java.util.ArrayList;
import java.util.List;

public class HashCodeRefactor extends AbstractRefactor {
    final  String warn = "该类重写了hashCode函数 但是没有重写equals函数";
    @Override
    public void refactor(Issue issue) {
        MethodDeclaration hashCodeMethod = (MethodDeclaration) issue.getIssueNode();
        tip(hashCodeMethod);
    }

    private void tip(MethodDeclaration hashCodeMethod) {
        BlockComment blockComment = new BlockComment();
        blockComment.setContent(warn);
        hashCodeMethod.setComment(blockComment);
    }

}
