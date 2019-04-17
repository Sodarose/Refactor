import formatter.FormatOptions;
import formatter.Formatter;
import io.FileUlits;
import java.util.Map;

public class FormatterTest {
  public static void main(String args[]){
    String source = FileUlits.readFile("/home/kangkang/IdeaProjects/w8"
        + "x/core/src/test/java/TestFile.java");
    Map<String,Object> options = FormatOptions.options("/home/kangkang/IdeaProjects"
        + "/w8x/core/src/main/resources/static/options.xml");
    source = Formatter.format(source,options);
    System.out.println(source);
  }
}
