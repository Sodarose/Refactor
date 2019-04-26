package refactor;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * 重构接口
 * */
public interface Refacoring {
  CompilationUnit performRefactoring();
}
