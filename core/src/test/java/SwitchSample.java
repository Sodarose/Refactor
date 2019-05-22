import Scanner.EnumT;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import io.FileUlits;
import ulits.FindNodeUlits;

import java.util.List;

public class SwitchSample {

    String agrs = "'asdas";
    EnumT e = new EnumT();
    enum EmployeeType {
        ENGINEER, SALESMAN, MANAGER;
    }

    public static int m_basicSalary;
    public static int m_commission;
    static EnumT.EmployeeType emp = EnumT.EmployeeType.ENGINEER;
    static int i = 10;

    //类变量
    public int PayAmount() throws Exception {
        EnumT.EmployeeType empType = EnumT.EmployeeType.ENGINEER;
        if (empType == EnumT.EmployeeType.ENGINEER) {
            System.out.println("asdasdasd");
        } else if (empType == EnumT.EmployeeType.SALESMAN) {
            System.out.println("asdasd");
        } else if (empType == EnumT.EmployeeType.MANAGER) {
            System.out.println("sssss");
        } else {
            System.out.println("sss");
        }
        return i;
    }

    //类变量
    public int Amount() throws Exception {
        EmployeeType empType = EmployeeType.ENGINEER;
        String args = "";
        if (pcs() == "as") {
            System.out.println("asdasdasd");
        } else if (pcs() == "asdsadas") {
            System.out.println("asdasd");
        } else if ("asdsad" == pcs()) {
            System.out.println("sssss");
        } else if ("assssss" == pcs()) {
            System.out.println("sssss");
        } else {
            System.out.println("sss");
        }
        return i;
    }

    private String pcs() {
        return "asdasd";
    }

    //local变量
    public int PayAmounts() throws Exception {
        EnumT.EmployeeType empType = EnumT.EmployeeType.ENGINEER;
        switch (empType) {
            case ENGINEER:
                System.out.println("asdasdasd");
                break;
            case SALESMAN:
                System.out.println("asdasd");
                break;
            case MANAGER:
                System.out.println("sssss");
                break;
            default:
                System.out.println("sss");
                break;
        }
        return i;
    }

    //函数的
    public void k() {
        String pc = "Tcp";
        if ("sasd".equals(sb())) {
            System.out.println("asdasd");
        } else if ("aad".equals(sb())) {
            System.out.println("asdaasdsasd");
        } else if ("".equals(sb())) {
            System.out.println("sssss");
        } else {
            System.out.println("sss");
        }
        return;
    }

    public void q() {

        switch (sb()) {
            case "sasd":
                break;
            case "aad":
                break;
            case "":
                System.out.println("sssss");
                break;
            default:
                System.out.println("sss");
                break;
        }
        return;
    }

    public void qs() {
        EmployeeType type = EmployeeType.ENGINEER;
        if (sbc() == EmployeeType.ENGINEER) {
        } else if (sbc() == EmployeeType.MANAGER) {
        } else if (sbc() == EmployeeType.SALESMAN) {
        } else {
            return;
        }
        return;
    }
    public void qsS() {
        EmployeeType type = EmployeeType.ENGINEER;
        switch (sbc()) {
            case ENGINEER:
                break;
            case MANAGER:
                break;
            case SALESMAN:
                break;
            default:
                return;
        }
        return;
    }

    private EmployeeType sbc() {
        return EmployeeType.ENGINEER;
    }

    private String sb(int i) {
        return null;
    }

    //字符串
    public void ps() {
        switch (e.getS()) {
            case "sasd":
                break;
            case "aad":
                break;
            case "":
                System.out.println("sssss");
                if(1==100){
                    return;
                }else if(2==100){
                    return;
                }else if (3==0){
                    return;
                }
                System.out.println("sss");
            default:
                System.out.println("sss");
                break;
        }
        return;
    }

    private String sb() {
        return "asd";
    }


    /**
     * switch再来一遍，接收枚举类型，判断输出相应的职位描述
     *
     * @param empType Employee type in enum
     * @return Description of each type
     */

    public int GetDescription(EnumT.EmployeeType empType) throws Exception {

        throw new Exception("no such employee type!");
    }

    public static void main(String[] args) {
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\SwitchSample.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<SimpleName> list = FindNodeUlits.findSimpleNameByName(unit, "empType", true);
        for (SimpleName variableDeclarationExpr : list) {
            if (variableDeclarationExpr.getParentNode().isPresent()) {
                String fullName = variableDeclarationExpr.getParentNode().get().getClass().getName();
                String name = fullName.substring(fullName.lastIndexOf(".") + 1);
                if (name.equals("Parameter")) {
                    Parameter parameter = (Parameter) variableDeclarationExpr.getParentNode().get();
                    String typeName = parameter.getType().asClassOrInterfaceType().asString();
                    System.out.println(parameter.getType().asClassOrInterfaceType().isUnknownType());
                    List<SimpleName> lit = FindNodeUlits.findSimpleNameByName(unit, typeName, true);
                    for (SimpleName field : lit) {
                        System.out.println(field.getParentNode().get().getClass().getName());
                    }
                }
            }
        }
    }


}