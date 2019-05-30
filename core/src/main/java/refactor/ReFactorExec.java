package refactor;

import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import model.Issue;
import model.IssueContext;
import model.JavaModelVo;
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

    public void factorAll() {
        for (Map.Entry<String, JavaModelVo> entry : Store.unitMaps.entrySet()) {
            JavaModelVo javaModelVo = entry.getValue();
            if (javaModelVo.isRefactor()) {
                continue;
            }
            CompilationUnit unit = javaModelVo.getUnit();
            //保存原本副本
            javaModelVo.setRefactUnit(unit.clone());
            runFactor(javaModelVo.getIssues());
            javaModelVo.setRefactor(true);
            javaModelVo.getIssues().clear();
        }
    }

    public void factorOne(String javaFileName) {
        JavaModelVo javaModelVo = Store.unitMaps.get(javaFileName);
        if (javaModelVo.isRefactor()) {
            return;
        }
        CompilationUnit unit = javaModelVo.getUnit();
        javaModelVo.setRefactUnit(unit.clone());
        runFactor(javaModelVo.getIssues());
        javaModelVo.setRefactor(true);
        javaModelVo.getIssues().clear();
    }

    /**
     * 执行重构的方法
     */
    private void runFactor(List<Issue> issues) {
        Iterator<Issue> it = issues.iterator();
        try {
            while (it.hasNext()) {
                Issue issue = it.next();
                Refactor refactor = (Refactor) Class.forName(issue.getRefactorName()).newInstance();
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
