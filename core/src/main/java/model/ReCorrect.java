package model;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import lombok.Data;

/**
 * 重构模型数据
 * */
@Data
public class ReCorrect {
    private String correctName;
    private String message;
    private String description;
    private Node correctNode;
    private Node issueNode;
    private String refactorName;
    private CompilationUnit node;
    private boolean status;
}
