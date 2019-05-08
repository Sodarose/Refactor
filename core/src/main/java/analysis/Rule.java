package analysis;

import com.github.javaparser.ast.CompilationUnit;
import model.Issue;

import java.util.List;

/**
 * 扫描规则文件
 * */
public interface Rule {
    /**
     * 扫描文件接口
     * */
    List<Issue> apply(List<CompilationUnit> units);
}
