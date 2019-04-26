package refactor;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;


@Getter
public class BaseVisitor<T extends ASTNode> extends ASTVisitor {
    private List<T> list = new ArrayList<>();
}
