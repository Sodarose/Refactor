package com.w8x.web.Service;

import com.w8x.web.model.Code;
import com.w8x.web.model.CodeShown;
import com.w8x.web.model.Overview;
import model.TreeNode;

/**
 * 核心服务
 * */
public interface RefactCoreService {
    Code runAnalysis(String filePath);
    String getJavaFileTree();
    CodeShown getJavaFileDetail(String filePath);
    Code<Overview> getOverview();
    Code<String> refactorAll();

    Code<String> analysisAgin();
}
