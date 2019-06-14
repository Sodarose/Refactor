import JavaParser.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import io.FileUlits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import nametest.splitName;
public class MethodNameTest {
    private static Pattern pattern = Pattern.compile("^[a-z|$][a-z0-9]*([A-Z][a-z0-9]*)*(DO|DTO|VO|DAO)?$");
    public static void main(String args[]){
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<MethodDeclaration> methodVisitor=new BaseVisitor<MethodDeclaration>(){
            @Override
            public void visit(MethodDeclaration n, Object arg) {

                    List<String> nameList=new ArrayList<String>();
                    try {
                        String name="";
                        nameList=splitName.split(n.getNameAsString());
                        for(String data:nameList){
                            if(name.equals("")){
                                name=name+data;
                                continue;
                            }
                            data=data.substring(0,1).toUpperCase()+data.substring(1);
                            name=name+data;
                        }
                        System.out.println(name);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                super.visit(n, arg);
            }
        };
        unit.accept(methodVisitor,null);
    }
}
