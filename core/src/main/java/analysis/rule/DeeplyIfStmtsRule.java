package analysis.rule;


import analysis.AbstractRule;
import com.github.javaparser.ast.CompilationUnit;
import model.Issue;

import java.util.List;

public class DeeplyIfStmtsRule extends AbstractRule {

    @Override
    public List<Issue> apply(List<CompilationUnit> units) {
        for(CompilationUnit unit:units){

        }
        return null;
    }

}
