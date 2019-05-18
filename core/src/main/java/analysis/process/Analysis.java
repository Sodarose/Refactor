package analysis.process;

import analysis.Rule;
import analysis.RuleLink;
import com.github.javaparser.ast.CompilationUnit;
import io.ParserProject;
import model.Issue;
import model.IssueContext;
import refactor.ReFactorExec;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * 扫描入口
 * @author kangkang
 * */
public class Analysis  {

    private  ExecutorService service;
    private CompletionService<IssueContext> completionService;
    private final int MAX_THREAD = 10;
    private int taskNum = 0;
    private List<CompilationUnit> units;

    public Analysis(){
        service = Executors.newFixedThreadPool(MAX_THREAD,new RuleThreadFactory());
        completionService = new ExecutorCompletionService<>(service);
    }

    public void analysis(String project){
        units = ParserProject.parserProject(project);
        List<Rule> rules = RuleLink.newInstance().readRuleLinkByXML();
        runAnalysis(units,rules);
    }

    public IssueContext results(){
        return collectResult();
    }

    private void runAnalysis(List<CompilationUnit> units, List<Rule> rules){
        Iterator<Rule> it = rules.iterator();
        while(it.hasNext()){
            Rule rule = it.next();
            if(rule.isRun()) {
                completionService.submit(new RuleRunnable(rule, units));
                ++taskNum;
            }
        }
        service.shutdown();
    }

    private IssueContext collectResult(){
        IssueContext context = new IssueContext();
        try {
            for (int i = 0; i < taskNum; i++) {
                IssueContext item = completionService.take().get();
                context.getIssues().addAll(item.getIssues());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return context;
    }

    public static void main(String []args){
        Analysis analysis = new Analysis();
        analysis.analysis("D:\\gitProject\\W8X");
        List<Issue> list = analysis.collectResult().getIssues();
        for(Issue issue:list){
            System.out.println(issue.getIssueNode());
        }
    }

}
