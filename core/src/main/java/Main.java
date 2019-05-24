import analysis.process.Analysis;
import com.github.javaparser.ast.Node;
import lombok.extern.log4j.Log4j2;
import refactor.ReFactorExec;

import java.util.Iterator;
import java.util.Set;

/**
 *
 */
@Log4j2
public class Main {
    public static void main(String[] args) {
        //分析
        System.out.println("开始扫描");
        Analysis analysis = new Analysis();
        analysis.analysis("E:\\w8x-dev");
        System.out.println("扫描结束");
        System.out.println("开始重构");
        //重构
        ReFactorExec reFactorExec = new ReFactorExec();
        reFactorExec.factor(analysis.results());
        System.out.println("重构结束");
    }
}
