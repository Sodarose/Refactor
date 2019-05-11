package analysis.rule;

import analysis.AbstractRuleVisitor;
import com.github.javaparser.ast.CompilationUnit;
import model.IssueContext;

import java.util.List;

/**
 * 转换为for语句会更好
 * */
public class WhileChangeForRule extends AbstractRuleVisitor {
    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        return getContext();
    }
}
