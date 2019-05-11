public class SwitchSample {


    enum EmployeeType {
        ENGINEER, SALESMAN, MANAGER;
    }

    public static int m_basicSalary;
    public static int m_commission;
    static EmployeeType emp = EmployeeType.ENGINEER;
    static int i =10;


    /**
     * switch语句接收枚举类型，判断输出相应的薪资算法
     *
     * @param empType Employee type in enum
     * @return Payment of each type
     */
    public int Pay(EmployeeType empType) throws Exception {
        if (empType == EmployeeType.ENGINEER) {
            return m_basicSalary;
        } else if (empType == EmployeeType.SALESMAN) {
            return m_basicSalary + m_commission;
        } else if (empType == EmployeeType.MANAGER) {
            return 2 * m_basicSalary;
        } else {
            throw new Exception("no such employee type!");
        }
    }


    public int PayAmount(EmployeeType empType) throws Exception {

        switch (empType) {
            case ENGINEER:
                return m_basicSalary;
            case SALESMAN:
                return m_basicSalary + m_commission;
            case MANAGER:
                return 2 * m_basicSalary;
            default:
                throw new Exception("no such employee type!");
        }
    }

    /**
     * switch再来一遍，接收枚举类型，判断输出相应的职位描述
     *
     * @param empType Employee type in enum
     * @return Description of each type
     */
    public String GetDescription(EmployeeType empType) throws Exception {
        switch (empType) {
            case ENGINEER:
                return "Coding, Debug, Optimization";
            case SALESMAN:
                return "Getting contracts";
            case MANAGER:
                return "Analysis, Scheduling, Reporting";
            default:
                throw new Exception("no such employee type!");
        }
    }

    public static void cp() {
        int i = 1;
        switch (i) {
            case 1:
                System.out.println(1);
                break;
            case 2:
                System.out.println(2);
            case 3:
                System.out.println(3);
            default:
                System.out.println(10);
        }
    }
    public static int q(){
        return 1;
    }
    public static void pc(){
        if(i==1){

        }else if(i==2){

        }
        if(i==3){

        }
        System.out.println(10);
    }
    public static void main(String args[]){
        cp();
    }
}