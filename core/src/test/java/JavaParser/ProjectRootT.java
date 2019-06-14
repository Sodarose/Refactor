package JavaParser;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class ProjectRootT {
    public static void main(String args[]) throws IOException {
        String root = "D:\\gitProject\\W8X";
        SourceRoot sourceRoot = new SourceRoot(Paths.get(root));
        List<ParseResult<CompilationUnit>> units = sourceRoot.tryToParse();
        Iterator<ParseResult<CompilationUnit>> it = units.iterator();
        System.out.println(units.size());
    }
}
