package io;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import sun.security.krb5.internal.PAData;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * read all java file by one project
 * */

public class ParserProject {
    public static List<CompilationUnit> parserProject(String path){
        List<CompilationUnit> units = new ArrayList<>();
        try {
            SourceRoot sourceRoot = new SourceRoot(Paths.get(path));
            List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
            Iterator<ParseResult<CompilationUnit>> it = parseResults.iterator();
            while (it.hasNext()) {
                ParseResult<CompilationUnit> parseResult = it.next();
                units.add(parseResult.getResult().get());
                it.remove();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return units;
    }
}
