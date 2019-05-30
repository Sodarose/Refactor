package model;

import com.github.javaparser.ast.CompilationUnit;
import lombok.Data;

import java.util.List;

@Data
public class JavaModelVo {
    private CompilationUnit unit;
    private CompilationUnit refactUnit;
    private boolean isRefactor;
    List<Issue> issues;
}
