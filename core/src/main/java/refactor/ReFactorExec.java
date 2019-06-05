package refactor;

import lombok.Data;
import model.Issue;
import model.JavaModel;
import model.Store;

import java.util.*;

/**
 * 重构执行类
 * 修改为单例存在
 *
 * @author kangkang
 */
@Data
public class ReFactorExec {
    /**
     * 执行全部重构
     */
    private final int IS_REFACTOR = 0;
    private final int SUCCESS = 1;
    private static ReFactorExec reFactorExec;

    public static ReFactorExec getInstance() {
        if (reFactorExec == null) {
            reFactorExec = new ReFactorExec();
        }
        return reFactorExec;
    }

    private ReFactorExec() {

    }


    /**
     * 执行重构的方法
     */
    public void runFactor(List<Issue> issues) {
        Iterator<Issue> it = issues.iterator();
        try {
            while (it.hasNext()) {
                Issue issue = it.next();

                Refactor refactor = (Refactor) Class.forName(issue.getRefactorName()).newInstance();
                JavaModel javaModel = issue.getJavaModel();

               if (javaModel == null) {
                   System.out.println("空");
                    continue;
                }
                if (javaModel.getCopyUnit() == null) {
                    javaModel.setCopyUnit(javaModel.getUnit().clone());
                }
                if (javaModel.getIssues() == null) {
                    javaModel.setIssues(new ArrayList<>());
                }
                javaModel.getIssues().addAll(issues);
                javaModel.setHasIssue(true);
                refactor.refactor(issue);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
