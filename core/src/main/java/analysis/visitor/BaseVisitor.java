package analysis.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BaseVisitor<T extends Node> extends VoidVisitorAdapter {
    private List<T> list = new ArrayList<>();
}
