import JavaParser.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.FileUlits;
import nametest.splitName;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class nametest {

    private static final Pattern PATTERN
            = Pattern.compile("^I?([A-Z][a-z0-9]+)+(([A-Z])|(DO|DTO|VO|DAO|BO|DAOImpl|YunOS|AO|PO))?$");
    public static void main(String args[])throws IOException {
        String source = FileUlits.readFile("E:\\w8x-dev\\core\\src\\test\\java\\testName.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        BaseVisitor<ClassOrInterfaceDeclaration> classvistor=new BaseVisitor<ClassOrInterfaceDeclaration>(){
            @Override
            public void visit(ClassOrInterfaceDeclaration n, Object arg)  {
                if(!(PATTERN.matcher(n.getNameAsString()).matches())){
                    List<String> nameList=new ArrayList<String>();

                    try {
                        String name="";
                        nameList=splitName.split(n.getNameAsString());
                        for(String data:nameList){
                            data=data.substring(0,1).toUpperCase()+data.substring(1);

                            name=name+data;
                        }
                        //n.setName(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                super.visit(n, arg);
            }
        };
        unit.accept(classvistor,null);
    }
}
