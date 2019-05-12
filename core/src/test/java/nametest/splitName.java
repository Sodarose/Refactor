package nametest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class splitName {

        public static List<String> split(String name) throws IOException {
            String copyname=name.toLowerCase();
            List<String> dataList=new ArrayList<String>();
            dataList=readData();

          System.out.println(1);
            List<String> nameList=new ArrayList<String>();
            for(String data:dataList){
                if(data.trim().length()<copyname.trim().length()) {
                   SundayUtil sundayUtil=new SundayUtil();
                   int pos=sundayUtil.sunday(copyname.trim(),data.trim());
                    if (pos != -1) {
                        String word = name.substring(pos, pos + data.length());
                        System.out.println(word);
                        nameList.add(word);
                    }
                }
            }
            return nameList;
        }
        public static List<String> readData() throws FileNotFoundException, IOException {
            List<String> nameList=new ArrayList<String>();
            FileReader reader = new FileReader("core/src/main/resources/static/data.txt");
            BufferedReader br = new BufferedReader(reader);
            String data=null;
            while ((data = br.readLine()) != null) {
                nameList.add(data);
            }
            br.close();
            reader.close();
            return nameList;
        }
}
