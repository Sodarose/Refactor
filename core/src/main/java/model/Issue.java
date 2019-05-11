package model;

import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import refactor.Refactor;

import java.util.List;

/**
 * 坏味道模型数据
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    private String issueName;
    private String description;
    private Node issueNode;
    private String className;
    private Node unitNode;
    private String ruleName;
    private Refactor refactor;
}
