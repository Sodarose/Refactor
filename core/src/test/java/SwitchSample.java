public class SwitchSample {



    enum EmployeeType{
        ENGINEER,SALESMAN,MANAGER;
    }
    public static int m_basicSalary;
    public static int m_commission;
    static EmployeeType emp = EmployeeType.ENGINEER;
    public static void main(String args[]){
        System.out.println(emp);
    }

    /**
     * switch语句接收枚举类型，判断输出相应的薪资算法
     * @param empType    Employee type in enum
     * @return           Payment of each type
     */
    public int Pay(EmployeeType empType) throws Exception {
        if (empType == EmployeeType.ENGINEER) {
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            return m_basicSalary;
        } else if (empType == EmployeeType.SALESMAN) {
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            return m_basicSalary + m_commission;
        } else if (empType == EmployeeType.MANAGER) {
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            System.out.println("你个大傻吊");
            return 2 * m_basicSalary;
        } else {
            throw new Exception("no such employee type!");
        }
    }


    public int PayAmount(EmployeeType empType) throws Exception
    {

        switch (empType)
        {
            case ENGINEER:
                {
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    System.out.println("你个大傻吊");
                    return m_basicSalary;
                }
            case SALESMAN:
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                return m_basicSalary + m_commission;
            case MANAGER:
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                System.out.println("你个大傻吊");
                return 2 * m_basicSalary;
            default:
                throw new Exception("no such employee type!");
        }
    }
    /**
     * switch再来一遍，接收枚举类型，判断输出相应的职位描述
     *
     * @param empType    Employee type in enum
     * @return           Description of each type
     */
    public String GetDescription(EmployeeType empType) throws Exception
    {
        switch (empType)
        {
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
}