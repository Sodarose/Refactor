public class SwitchSample {


    enum EmployeeType {
        ENGINEER, SALESMAN, MANAGER;
    }

    public static int m_basicSalary;
    public static int m_commission;
    static EmployeeType emp = EmployeeType.ENGINEER;
    static int i = 10;

    public int PayAmount(EmployeeType empType) throws Exception {

        switch (empType) {
            case ENGINEER:
                System.out.println("sss");
            case SALESMAN:
                System.out.println("sss");
            case MANAGER:
                System.out.println("sss");
            default:
                throw new Exception("no such employee type!");
        }
    }

    public void k() {
        String t = "s";
        switch (t) {
            case "1":
                System.out.println("sdasds");
                break;
            case "2":
                System.out.println("qeqeqeq");
                break;
            case "3":
                System.out.println("asdasd");
                break;
            default:
                System.out.println("asdsad");
        }
    }

    /**
     * switch再来一遍，接收枚举类型，判断输出相应的职位描述
     *
     * @param empType Employee type in enum
     * @return Description of each type
     */

    public int GetDescription(EmployeeType empType) throws Exception {

        throw new Exception("no such employee type!");
    }


}