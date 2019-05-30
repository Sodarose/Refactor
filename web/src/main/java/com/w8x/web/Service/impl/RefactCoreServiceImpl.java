package com.w8x.web.Service.impl;

import api.AnalysisApi;
import com.alibaba.fastjson.JSON;
import com.github.javaparser.utils.ProjectRoot;
import com.w8x.web.Service.RefactCoreService;
import com.w8x.web.model.Code;
import com.w8x.web.model.CodeShown;
import com.w8x.web.model.IssueShow;
import formatter.FormatOptions;
import formatter.Formatter;
import io.ParserProject;
import model.Issue;
import model.JavaModelVo;
import model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用Core服务
 *
 * @author Administrator*/
@Service
public class RefactCoreServiceImpl implements RefactCoreService {

    private AnalysisApi analysisApi = AnalysisApi.getInstance();
    private Formatter formatter = Formatter.getInstance();
    /**
     * 进行 分析
     * */
    @Override
    public Code runAnalysis(String filePath) {
         if(analysisApi.analysis(filePath)){
            return Code.createCode(200,null,"扫描成功");
         }
        return Code.createCode(404,null,"扫描失败");
    }

    @Override
    public String getJavaFileTree() {
        return analysisApi.getJavaFileTree();
    }

    @Override
    public CodeShown getJavaFileDetail(String filePath) {
        JavaModelVo vo = analysisApi.getJavaModelVo(filePath);
        if(vo==null){
            return null;
        }
        CodeShown codeShown = new CodeShown();
        codeShown.setOriginalCode(formatter.format(vo.getUnit().toString(), FormatOptions.getOptions()));
        if(vo.getRefactUnit()!=null){
            codeShown.setRefactCode(formatter.format(vo.getRefactUnit().toString(), FormatOptions.getOptions()));
        }
        if(vo.getIssues()!=null&&vo.getIssues().size()!=0){
            List<IssueShow> issueShows =new ArrayList<>();
            for(Issue issue :vo.getIssues()){
                IssueShow issueShow = new IssueShow();
                issueShow.setBeginLine(issue.getIssueNode().getRange().get().begin.line);
                issueShow.setEndLine(issue.getIssueNode().getRange().get().end.line);
                issueShow.setIssueMessage(issue.getDescription());
                issueShow.setIssueName(issue.getIssueName());
            }
            codeShown.setIssueShows(issueShows);
        }
        return codeShown;
    }

}
