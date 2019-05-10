package analysis.process;


import analysis.Rule;
import com.github.javaparser.ast.CompilationUnit;
import model.IssueContext;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 具体执行线程
 * */
public class RuleRunnable implements Callable<IssueContext>{
    private Rule rule;
    private List<CompilationUnit> units;

    public RuleRunnable(Rule rule,List<CompilationUnit> units){
        this.rule = rule;
        this.units = units;
    }

    @Override
    public IssueContext call() throws Exception {
        return rule.apply(units);
    }
}
