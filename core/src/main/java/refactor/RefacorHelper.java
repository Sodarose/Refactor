package refactor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 *
 * 用于搜索
 * */
public class RefacorHelper {
    /**
     * 将文件列表转换成AST列表
     * */
    public List<CompilationUnit> solveFiles(List<String> sources){
      ASTParser parser = ASTParser.newParser(AST.JLS11);
      List<CompilationUnit> units = new ArrayList<>();
      Iterator<String> it = sources.iterator();
      String source = null;
      while(it.hasNext()){
        source = it.next();
        parser.setSource(source.toCharArray());
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        units.add(unit);
      }
      return units;
    }



}
