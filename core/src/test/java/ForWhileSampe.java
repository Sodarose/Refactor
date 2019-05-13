@SuppressWarnings("ALL")
public class ForWhileSampe {
    @SuppressWarnings("ForLoopReplaceableByWhile")
    public void cp() {
        int i = 100;
        for (; ; ) {
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            if (i == 100) {
                break;
            }
        }

        for (; i < 100; ) {
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            if (i == 100) {
                break;
            }
        }

        T:for (; ; ) {
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            for (; ; ) {
                System.out.println("sdasdasd");
                System.out.println("sdasdasd");
                for (; ; ) {
                    System.out.println("sdasdasd");
                    System.out.println("sdasdasd");
                    if (i == 100) {
                        break T;
                    }
                }
            }

        }
    }

    //如果一个方法内对这个变量进行了多次循环 则不做重构
    public void pc() {

        //第一种类型
        System.out.println("sdasdasd");
        System.out.println("sdasdasd");
        int i = 0;
        System.out.println("sdasdasd");
        System.out.println("sdasdasd");
        while (i < 100) {
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
            i++;
            System.out.println("sdasdasd");
            System.out.println("sdasdasd");
        }
        System.out.println("sdasdasd");
        System.out.println("sdasdasd");

        //第二种类型
        int j = 0;
        while (j < 1000) {
            j++;
            i++;
        }

        //第三种类型
        while (i < 1000 && j < 1000) {
            i++;
            j++;
        }
    }

}