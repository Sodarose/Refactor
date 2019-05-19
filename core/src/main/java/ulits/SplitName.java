package ulits;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SplitName {

        public static List<String> split(String name) throws IOException {
            String copyname=name.toLowerCase();
            List<String> dataList=new ArrayList<String>();
            dataList=readData();
            Map<Integer,String> nameMap=new TreeMap<Integer, String>();
            List<String> nameList=new ArrayList<String>();
            for(String data:dataList){
                if(data.trim().length()<copyname.trim().length()) {
                   SundayUtil sundayUtil=new SundayUtil();
                   int pos=sundayUtil.sunday(copyname.trim(),data.trim());
                    if (pos != -1) {
                        nameMap.put(pos,name.substring(pos,pos+data.length()));
                    }
                }
            }
            nameMap=sortMapByKey(nameMap);
            for(Integer key:nameMap.keySet()){
                nameList.add(nameMap.get(key));
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
        public static Map<Integer,String> sortMapByKey(Map<Integer,String> map){
            if(map == null || map.isEmpty()){
                return null;
            }
            Map<Integer,String> sortMap=new TreeMap<Integer, String>(new MapKeyComparator());
            sortMap.putAll(map);
            return sortMap;
        }
}

class MapKeyComparator implements Comparator<Integer>{

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
