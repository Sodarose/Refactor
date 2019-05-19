package nametest;

import java.io.BufferedReader;
import java.io.FileReader;

/*
按行读取内容文件
 */
public class readfile {
    public static void main(String[] args) throws Exception{

            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader("core/src/main/resources/static/data.txt");
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null) {
                sb.append(str + "\r\n");

            }

            br.close();
            reader.close();

        }
        }


