package refactor;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class BaseVisitorByJP <T> extends VoidVisitorAdapter<Object> {
    private List<T> list = new ArrayList<>();
}
