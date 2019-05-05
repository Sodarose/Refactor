import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import io.FileUlits;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import refactor.BaseVisitorByJP;


public class FormatterTest {


  public static void main(String args[]) {
    String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\TestFile.java");
    CompilationUnit unit = StaticJavaParser.parse(source);
    BaseVisitorByJP<MethodDeclaration> visitorByJP = new BaseVisitorByJP<MethodDeclaration>() {
      @Override
      public void visit(MethodDeclaration n, Object arg) {
        if (n.getName().getIdentifier().equals("getPayAmount")) {
          getList().add(n);
        }
      }
    };
    unit.accept(visitorByJP, null);
    MethodDeclaration method = visitorByJP.getList().get(0);
    BaseVisitorByJP<IfStmt> visitorByJP1 = new BaseVisitorByJP<IfStmt>() {
      @Override
      public void visit(IfStmt n, Object arg) {
        getList().add(n);
        super.visit(n, null);
      }
    };
    method.accept(visitorByJP1, null);
    Iterator<IfStmt> it = visitorByJP1.getList().iterator();
    while (it.hasNext()) {
      sloveHasElse(it.next());
      it.remove();
    }


    /*改造retrun*/
    modifyReturn(method);
    System.out.println(method);
  }

  private static void modifyReturn(MethodDeclaration method){
    if(method.getType().isVoidType()){
      Iterator<Statement> it =  method.getBody().get().getStatements().iterator();
      ReturnStmt returnStmt = null;
      while(it.hasNext()){
        Statement statement = it.next();
        if(statement.isReturnStmt()){
          returnStmt = statement.asReturnStmt();
          break;
        }
      }
      method.getBody().get().remove(returnStmt);
      return;
    }

  }

  private static void mergeIf(MethodDeclaration method){

  }
  /**
   * 有else 去else 抽取函数体 放入父亲
   */
  private static void sloveHasElse(IfStmt ifs) {
    //得到父亲的返回值,如果父亲没有返回在则返回空返回值
    ReturnStmt returnStmt = getParentReturn(ifs);
    /** 处理if语句*/
    sloveThem(ifs, returnStmt);
    /*处理else 语句*/
    if (ifs.hasElseBlock()) {
      sloveElse(ifs, returnStmt);
    }
    /*删除父亲的返回语句*/

    collectReturnStmt(ifs,returnStmt);
  }

  private static ReturnStmt getParentReturn(IfStmt ifs) {
    BlockStmt blockStmt = (BlockStmt) ifs.getParentNode().get();
    ReturnStmt returnStmt = null;
    Iterator<Statement> it = blockStmt.getStatements().iterator();
    while (it.hasNext()) {
      Statement statement = it.next();
      if(statement.isReturnStmt()){
        returnStmt = statement.asReturnStmt();
        break;
      }
    }
    if (returnStmt == null) {
      returnStmt = new ReturnStmt();
    }
    return returnStmt;
  }


  private static void sloveThem(IfStmt ifs, ReturnStmt returnStmt) {
    BlockStmt blockStmt = ifs.getThenStmt().asBlockStmt();
    if (check(blockStmt)) {
      blockStmt.getStatements().add(returnStmt);
    }
  }

  private static void sloveElse(IfStmt ifs, ReturnStmt returnStmt) {
    BlockStmt blockStmt = (BlockStmt) ifs.getElseStmt().get();
    if (check(blockStmt)) {
      blockStmt.getStatements().add(returnStmt);
    }
    BlockStmt parent = (BlockStmt) ifs.getParentNode().get();
    parent.getStatements().addAll(blockStmt.getStatements());
    ifs.removeElseStmt();
  }

  private static boolean check(BlockStmt blockStmt) {
    Iterator<Statement> it = blockStmt.getStatements().iterator();
    Boolean has = true;
    while (it.hasNext()) {
      if(it.next().isReturnStmt()){
        has = false;
        return has;
      }
    }
    return has;
  }

  private static void collectReturnStmt(IfStmt ifs, ReturnStmt returnStmt) {

  }

  private static void deleteReturnStmt(List<Map<Node,ReturnStmt>> deleteList){

  }

  /**
   * 没有else 取反
   */
  private static void sloveNotElse(IfStmt ifs) {

  }

}
