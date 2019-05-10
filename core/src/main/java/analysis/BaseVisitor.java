package analysis;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class BaseVisitor<T extends Node> extends VoidVisitorAdapter {
    private Node caller = null;
    private List<T> list = new ArrayList<>();
}