package analysis;


import model.ReCorrect;

import javax.tools.JavaCompiler;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * 采用fork/join方式来进行多线程扫描
 * */
public class Analysis extends RecursiveTask<List<ReCorrect>> {
    @Override
    protected List<ReCorrect> compute() {
        return null;
    }
}
