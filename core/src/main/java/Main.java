import analysis.process.Analysis;
import com.github.javaparser.ast.Node;
import refactor.ReFactorExec;

import java.util.Iterator;
import java.util.Set;

/**
 *
 * */
public class Main {
    public static void main(String []args){
        //分析
        Analysis analysis = new Analysis();
        analysis.analysis("D:\\gitProject\\W8X\\core");
        //重构
        ReFactorExec reFactorExec = new ReFactorExec();
        reFactorExec.factor(analysis.results());
        Set<Node> nodeSet = reFactorExec.getNodeSet();
        Iterator<Node> it = nodeSet.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }
}
