package nametest;

public class SundayTest {
    public static void main(String[] agrs){
       SundayTest sundayTest=new SundayTest();
        System.out.println(sundayTest.strStr("testname","name"));
    }
    public int strStr(String haystack, String needle) {
        int m=haystack.length(), n=needle.length();
        int[] occ=getOCC(needle);
        int jump=0;
        for(int i=0;i<=m-n; i+=jump){
            int j=0;
            while(j<n&&haystack.charAt(i+j)==needle.charAt(j))
                j++;
            if(j==n)
                return i;
            jump=i+n<m ? n-occ[haystack.charAt(i+n)] : 1;
        }
        return -1;
    }

    public int[] getOCC(String p){
        int[] occ=new int[256];
        for(int i=0;i<occ.length;i++)
            occ[i]=-1;
        for(int i=0;i<p.length();i++)
            occ[p.charAt(i)]=i;
        return occ;
    }
}
