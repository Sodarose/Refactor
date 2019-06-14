
public class ForWhileSampe {
    private int is = 0;

    public void cp() {
         int i = 100;
         for (int a = 0,b=0,c=0; ; ) {
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             if (i == 100) {
                 break;
             }
             i++;
             i++;
             i++;
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
         double l = 0.0;
         while (l<100.0){
             l++;
         }
     }

     //如果一个方法内对这个变量进行了多次循环 则不做重构
     public void pc() {

         //第一种类型
         System.out.println("sdasdasd");
         System.out.println("sdasdasd");
         int i = 0,q=0;
         int a = 0;
         a = 100;
         a++;
         System.out.println("sdasdasd");
         System.out.println("sdasdasd");
         while (i<a&&q<1000) {
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             System.out.println("sdasdasd");
             i+=2;
         }
         System.out.println("sdasdasd");
         System.out.println("sdasdasd");

     }
    public void t() {

        int i = 0;
        T:while (i < 100) {
            System.out.println("sadasd");
            i++;
            System.out.println("sdasds");
        }
        int k = 0,j=0;
        T:while (j<k) {

            j++;
            k++;
        }
    }
}