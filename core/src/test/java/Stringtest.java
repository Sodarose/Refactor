public class Stringtest {
    public static void main(String[] args){
        String s="org.apache.zookeeper.lo";
        int index=s.lastIndexOf(".");
        System.out.println(s.substring(0,index+1));
    }
}
