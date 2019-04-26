import io.FileUlits;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import refactor.BaseVisitor;
import sun.security.krb5.internal.ASRep;

public class FormatterTest {

  public static void main(String args[]) {
    String source = FileUlits.readFile("/home/kangkang/IdeaProjects/w8x/core/src"
        + "/test/java/TestFile.java");
    ASTParser astParser = ASTParser.newParser(AST.JLS11);
    astParser.setResolveBindings(true);
    astParser.setSource(source.toCharArray());
    CompilationUnit unit = (CompilationUnit) astParser.createAST(null);
    MethodDeclaration mothod = null;
    BaseVisitor<MethodDeclaration> visitor = new BaseVisitor<MethodDeclaration>() {
      @Override
      public boolean visit(MethodDeclaration node) {
        if (node.getName().getIdentifier().equals("cp")) {
          getList().add(node);
        }
        return true;
      }
    };
    unit.accept(visitor);
    if (visitor.getList().size() == 0) {
      return;
    }
    mothod = visitor.getList().get(0);
    System.out.println();
    BaseVisitor<IfStatement> conVisitor = new BaseVisitor<IfStatement>() {
      @Override
      public boolean visit(IfStatement node) {
        getList().add(node);
        return false;
      }
    };
    mothod.accept(conVisitor);
    Iterator<IfStatement> it = conVisitor.getList().iterator();
    while (it.hasNext()) {
      IfStatement ifs = it.next();
      it.remove();
      if (ifs.getElseStatement() != null) {
          sloveHasElse(ifs);
          continue;
      }
      sloveNotElse(ifs);
    }
    System.out.println("--------------------------------------------");
    //System.out.println(mothod);
  }
  /**
   * 有else 去else 抽取函数体 放入父亲
   * */
  private static void sloveHasElse(IfStatement ifs) {
    
    System.out.println(ifs.getElseStatement());
  }

  /**
   * 没有else 取反
   * */
  private static void sloveNotElse(IfStatement ifs) {

  }
}
