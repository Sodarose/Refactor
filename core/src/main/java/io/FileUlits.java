package io;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUlits {

    public static String readFile(String filePath){
      String source = null;
      try{
        source = new String(Files.readAllBytes(Paths.get(filePath)),"UTF-8");
      }catch (IOException e){
        e.printStackTrace();
      }
      return source;
    }

    public static void writeFile(String filePath,String source){
      try{
        PrintWriter print = new PrintWriter(filePath);
        print.write(source);
        print.close();
      }catch (IOException e){

      }
    }
}
