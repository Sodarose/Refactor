import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

public class Demo {
  public static void main(String[] args) {
    AST ast = AST.newAST(AST.JLS11);
    CompilationUnit compilationUnit = ast.newCompilationUnit();

    //创建类
    TypeDeclaration programClass = ast.newTypeDeclaration();
    programClass.setName(ast.newSimpleName("Program"));
    programClass.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
    compilationUnit.types().add(programClass);

    //创建包
    PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
    packageDeclaration.setName(ast.newName("com.aptech.lzh"));
    compilationUnit.setPackage(packageDeclaration);

    //要导入的包
    String[] imports = {"java.util.Date", "java.util.Random"};
    for(String imp : imports){
      ImportDeclaration importDeclaration = ast.newImportDeclaration();
      importDeclaration.setName(ast.newName(imp));
      compilationUnit.imports().add(importDeclaration);
    }

    //创建一个main方法
    {
      MethodDeclaration main = ast.newMethodDeclaration();
      main.setName(ast.newSimpleName("main"));
      main.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
      main.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
      main.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));
      programClass.bodyDeclarations().add(main);
      Block mainBlock = ast.newBlock();
      main.setBody(mainBlock);

      //给main方法定义String[]参数
      SingleVariableDeclaration mainParameter = ast.newSingleVariableDeclaration();
      mainParameter.setName(ast.newSimpleName("arg"));
      mainParameter.setType(ast.newArrayType(ast.newSimpleType(ast.newName("String"))));
      main.parameters().add(mainParameter);

      //创建Pragram对象: Program program=new Program();
      VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
      fragment.setName(ast.newSimpleName("program"));
      VariableDeclarationStatement statement = ast.newVariableDeclarationStatement(fragment);
      statement.setType(ast.newSimpleType(ast.newSimpleName("Program")));

      ClassInstanceCreation classInstanceCreation = ast.newClassInstanceCreation();
      classInstanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Program")));

      fragment.setInitializer(classInstanceCreation);

      mainBlock.statements().add(statement);

      //调用getString方法:String r = program.getString("中国");
      MethodInvocation methodInvocation = ast.newMethodInvocation();
      methodInvocation.setExpression(ast.newSimpleName("program"));
      methodInvocation.setName(ast.newSimpleName("getString"));

      //String参数
      StringLiteral stringLiteral = ast.newStringLiteral();
      stringLiteral.setLiteralValue("中国");
      methodInvocation.arguments().add(stringLiteral);

      //创建变量
      VariableDeclarationFragment fragment2 = ast.newVariableDeclarationFragment();
      fragment2.setName(ast.newSimpleName("r"));
      VariableDeclarationStatement statement3 = ast.newVariableDeclarationStatement(fragment2);
      statement3.setType(ast.newSimpleType(ast.newSimpleName("String")));
      fragment2.setInitializer(methodInvocation);

      mainBlock.statements().add(statement3);


      //输出r的值: System.out.println(r);
      MethodInvocation methodInvocation2 = ast.newMethodInvocation();
      methodInvocation2.setExpression(ast.newName("System.out"));
      methodInvocation2.setName(ast.newSimpleName("println"));
      methodInvocation2.arguments().add(ast.newSimpleName("r"));
      ExpressionStatement statement2 = ast.newExpressionStatement(methodInvocation2);

      mainBlock.statements().add(statement2);
    }

    //构造方法
    {
      MethodDeclaration constructorMethod = ast.newMethodDeclaration();
      constructorMethod.setConstructor(true);
      constructorMethod.setName(ast.newSimpleName("Program"));
      constructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
      {
        //基本类型的参数
        SingleVariableDeclaration p1 = ast.newSingleVariableDeclaration();
        p1.setName(ast.newSimpleName("a"));
        p1.setType(ast.newPrimitiveType(PrimitiveType.INT));

        //int[]类型的参数
        SingleVariableDeclaration p2 = ast.newSingleVariableDeclaration();
        p2.setName(ast.newSimpleName("b"));
        p2.setType(ast.newArrayType(ast.newPrimitiveType(PrimitiveType.INT)));

        //引用类型的参数
        SingleVariableDeclaration p3 = ast.newSingleVariableDeclaration();
        p3.setName(ast.newSimpleName("c"));
        p3.setType(ast.newSimpleType(ast.newName("Integer")));
        p3.modifiers().add(ast.newModifier(ModifierKeyword.FINAL_KEYWORD));

        constructorMethod.parameters().add(p1);
        constructorMethod.parameters().add(p2);
        constructorMethod.parameters().add(p3);
      }

      Block constructBlock = ast.newBlock();
      constructorMethod.setBody(constructBlock);

      programClass.bodyDeclarations().add(constructorMethod);

      SuperConstructorInvocation superConstructorInvocation = ast.newSuperConstructorInvocation();
      constructBlock.statements().add(superConstructorInvocation);
      superConstructorInvocation.arguments().add(ast.newNullLiteral());
    }

        /*定义一个方法,形如:
        public String getString(String name){
            String newString = name + "你好";
            return newString;
        }
        */

    {
      MethodDeclaration getString = ast.newMethodDeclaration();
      getString.setName(ast.newSimpleName("getString"));
      getString.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
      SingleVariableDeclaration p = ast.newSingleVariableDeclaration();
      p.setName(ast.newSimpleName("p"));
      p.setType(ast.newSimpleType(ast.newName("String")));
      getString.parameters().add(p);
      getString.setReturnType2(ast.newSimpleType(ast.newSimpleName("String")));


      //创建块
      Block block = ast.newBlock();
      getString.setBody(block);
      programClass.bodyDeclarations().add(getString);

      //方法内容----定义String变量
      VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
      fragment.setName(ast.newSimpleName("newString"));
      VariableDeclarationStatement statement = ast.newVariableDeclarationStatement(fragment);

      //String newString = "初始值";
            /*StringLiteral stringLiteral2 = ast.newStringLiteral();
            stringLiteral2.setLiteralValue("初始值");
            fragment.setInitializer(stringLiteral2);*/

      ClassInstanceCreation classInstanceCreation = ast.newClassInstanceCreation();
      classInstanceCreation.setType(ast.newSimpleType(ast.newName("String")));
      SingleVariableDeclaration p1 = ast.newSingleVariableDeclaration();
      StringLiteral stringLiteral3 = ast.newStringLiteral();
      stringLiteral3.setLiteralValue("初始值");
      classInstanceCreation.arguments().add(stringLiteral3);
      fragment.setInitializer(classInstanceCreation);

      statement.setType(ast.newSimpleType(ast.newName("String")));

      Assignment assignment = ast.newAssignment();
      assignment.setLeftHandSide(ast.newSimpleName("newString"));
      StringLiteral stringLiteral = ast.newStringLiteral();
      stringLiteral.setLiteralValue("你好");

      assignment.setRightHandSide(stringLiteral);
      assignment.setOperator(Operator.ASSIGN);
      ExpressionStatement statement2 = ast.newExpressionStatement(assignment);

      block.statements().add(statement);
      block.statements().add(statement2);

      //方法调用
      MethodInvocation methodInvocation = ast.newMethodInvocation();
      methodInvocation.setExpression(ast.newName("newString"));
      methodInvocation.setName(ast.newSimpleName("index")); //方法名
      StringLiteral stringLiteral2 = ast.newStringLiteral();
      stringLiteral2.setLiteralValue("值");
      methodInvocation.arguments().add(stringLiteral2);

      VariableDeclarationFragment fragment2 = ast.newVariableDeclarationFragment();
      fragment2.setInitializer(methodInvocation);
      fragment2.setName(ast.newSimpleName("result"));
      VariableDeclarationStatement statement3 = ast.newVariableDeclarationStatement(fragment2);
      statement3.setType(ast.newSimpleType(ast.newName("String")));

      block.statements().add(statement3);

      StringLiteral stringLiteral4 = ast.newStringLiteral();
      stringLiteral4.setLiteralValue("你好");

      InfixExpression infixExpression = ast.newInfixExpression();
      infixExpression.setLeftOperand(ast.newName("name"));
      infixExpression.setOperator(org.eclipse.jdt.core.dom.InfixExpression.Operator.PLUS);
      infixExpression.setRightOperand(stringLiteral4);

      Assignment assignment2 = ast.newAssignment();
      assignment2.setLeftHandSide(ast.newSimpleName("newString"));
      assignment2.setOperator(Operator.ASSIGN);
      assignment2.setRightHandSide(infixExpression);

      ExpressionStatement statement4 = ast.newExpressionStatement(assignment2);

      block.statements().add(statement4);

      ReturnStatement rs = ast.newReturnStatement();
      rs.setExpression(ast.newName("newString"));
      block.statements().add(rs);
    }

    /**
     * 定义一个方法,形如:
     * public String isOdd(int a) throws NullPointerException, Exception{
     *     if(a < 0) throw new Exception("数字不能为负数");
     *
     *     if(a % 2 == 0){
     *         return "偶数";
     *     }else{
     *         System.out.println("完");
     *         return "奇数";
     *     }
     */
    {
      MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
      methodDeclaration.setName(ast.newSimpleName("isOdd"));
      methodDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
      methodDeclaration.setReturnType2(ast.newSimpleType(ast.newSimpleName("String")));
      //设置参数
      SingleVariableDeclaration singleVariableDeclaration = ast.newSingleVariableDeclaration();
      singleVariableDeclaration.setName(ast.newSimpleName("a"));
      singleVariableDeclaration.setType(ast.newPrimitiveType(PrimitiveType.INT));
      methodDeclaration.parameters().add(singleVariableDeclaration);

      //抛出异常
      methodDeclaration.thrownExceptionTypes().add(ast.newSimpleName("NullPointerException"));
      methodDeclaration.thrownExceptionTypes().add(ast.newSimpleName("Exception"));

      //创建块{}
      Block isOddBlock = ast.newBlock();
      methodDeclaration.setBody(isOddBlock);

      //创建if与异常
      IfStatement ifStatement = ast.newIfStatement();
      //表达式 a < 0
      InfixExpression infixExpression = ast.newInfixExpression();
      infixExpression.setLeftOperand(ast.newSimpleName("a"));
      infixExpression.setOperator(org.eclipse.jdt.core.dom.InfixExpression.Operator.LESS);

      NumberLiteral numberLiteral = ast.newNumberLiteral("0");
      infixExpression.setRightOperand(numberLiteral);

      ifStatement.setExpression(infixExpression);

      //设置if中的内容
      ThrowStatement throwStatement = ast.newThrowStatement();
      ClassInstanceCreation classInstanceCreation = ast.newClassInstanceCreation();
      classInstanceCreation.setType(ast.newSimpleType(ast.newSimpleName("Exception")));
      StringLiteral stringLiteral = ast.newStringLiteral();
      stringLiteral.setLiteralValue("数字不能为负数");
      classInstanceCreation.arguments().add(stringLiteral);
      throwStatement.setExpression(classInstanceCreation);
      ifStatement.setThenStatement(throwStatement);

      //if(a % 2 == 0)
      IfStatement ifStatement2 = ast.newIfStatement();
      InfixExpression infixExpression2 = ast.newInfixExpression();
      infixExpression2.setLeftOperand(ast.newSimpleName("a"));
      infixExpression2.setOperator(org.eclipse.jdt.core.dom.InfixExpression.Operator.REMAINDER);
      NumberLiteral numberLiteral2 = ast.newNumberLiteral("2");
      infixExpression2.setRightOperand(numberLiteral2);

      InfixExpression infixExpression3 = ast.newInfixExpression();
      infixExpression3.setLeftOperand(infixExpression2);
      infixExpression3.setOperator(org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS);
      NumberLiteral numberLiteral3 = ast.newNumberLiteral("0");
      infixExpression3.setRightOperand(numberLiteral3);

      ifStatement2.setExpression(infixExpression3);

      //return "偶数";
      ReturnStatement returnStatement = ast.newReturnStatement();
      StringLiteral stringLiteral2 = ast.newStringLiteral();
      stringLiteral2.setLiteralValue("偶数");
      returnStatement.setExpression(stringLiteral2);
      ifStatement2.setThenStatement(returnStatement);

      //else
      Block elseBlock = ast.newBlock();
      MethodInvocation methodInvocation = ast.newMethodInvocation();
      methodInvocation.setExpression(ast.newName("System.out"));
      methodInvocation.setName(ast.newSimpleName("println"));
      StringLiteral stringLiteral4 = ast.newStringLiteral();
      stringLiteral4.setLiteralValue("完");
      methodInvocation.arguments().add(stringLiteral4);
      ExpressionStatement statement = ast.newExpressionStatement(methodInvocation);
      elseBlock.statements().add(statement);

      ReturnStatement returnStatement2 = ast.newReturnStatement();
      StringLiteral stringLiteral3 = ast.newStringLiteral();
      stringLiteral3.setLiteralValue("奇数");
      returnStatement2.setExpression(stringLiteral3);
      elseBlock.statements().add(returnStatement2);

      ifStatement2.setElseStatement(elseBlock);

      isOddBlock.statements().add(ifStatement);
      isOddBlock.statements().add(ifStatement2);
      programClass.bodyDeclarations().add(methodDeclaration);
    }

    //System.out.println(compilationUnit.toString());

  }
}