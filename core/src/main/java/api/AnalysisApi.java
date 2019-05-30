package api;

import analysis.process.Analysis;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import model.*;
import refactor.ReFactorExec;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisApi {

    private static AnalysisApi instance;

    public static AnalysisApi getInstance() {
        if (instance == null) {
            instance = new AnalysisApi();
        }
        return instance;
    }

    /**
     * 项目扫描接口
     */
    public boolean analysis(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        Analysis analysis = new Analysis();
        analysis.analysis(path);
        //得到issueContext
        Store.issueContext = analysis.results();
        //数据处理
        organizeData();
        Store.run = true;
        return Store.unitMaps != null;
    }

    /**
     * 数据处理
     */
    private void organizeData() {
        /**
         * 整理数据
         * */
        //合并unit和issue
        Map<String, JavaModelVo> unitMaps = Store.unitMaps;
        for (Issue issue : Store.issueContext.getIssues()) {
            for(JavaModelVo javaModelVo : unitMaps.values()){
                if(javaModelVo.getUnit().equals(issue.getUnitNode())){
                    if(javaModelVo.getIssues()==null){
                        javaModelVo.setIssues(new ArrayList<>());
                    }
                    javaModelVo.getIssues().add(issue);
                }
            }
        }

        //得到文件树 //这个功能可以和parser操作合并
        TreeNode root = new TreeNode();
        ProjectRoot projectRoot = Store.projectRoot;
        root.setFile(false);
        String rootNmae = projectRoot.getRoot().toFile().getName();
        root.setFileName(rootNmae);
        root.setRealPath(projectRoot.getRoot().toFile().getPath());
        root.setChildren(new ArrayList<>());
        for(SourceRoot sourceRoot:projectRoot.getSourceRoots()){
            String path = sourceRoot.getRoot().toFile().getPath();
            String name =  path.substring(path.indexOf(rootNmae)+rootNmae.length()+1);
            TreeNode sourceTree = new TreeNode();
            root.getChildren().add(sourceTree);
            sourceTree.setRealPath(path);
            sourceTree.setFileName(name);
            sourceTree.setFile(false);
            sourceTree.setHasIssue(false);
            sourceTree.setChildren(new ArrayList<>());
            createFileTree(sourceRoot.getRoot().toFile(),sourceTree);
        }
        Store.rootNode = root;
    }

    private void createFileTree(File file,TreeNode treeNode){
        if(file.isFile()&&file.getName().endsWith(".java")){
            TreeNode fileNode = new TreeNode();
            fileNode.setRealPath(file.getPath());
            fileNode.setFileName(file.getName());
            fileNode.setFile(true);
            JavaModelVo javaModelVo = Store.unitMaps.get(file.getPath());
            if(javaModelVo!=null&&javaModelVo.getIssues()!=null&&javaModelVo.getIssues().size()!=0){
                fileNode.setHasIssue(true);
            }
            treeNode.getChildren().add(fileNode);
        }
        if(file.isDirectory()){
            TreeNode dirNode = new TreeNode();
            dirNode.setHasIssue(false);
            dirNode.setFile(false);
            dirNode.setFileName(file.getName());
            dirNode.setRealPath(file.getPath());
            dirNode.setChildren(new ArrayList<>());
            treeNode.getChildren().add(dirNode);
            for(File f:file.listFiles()){
                createFileTree(f,dirNode);
            }
        }
    }

    public void refactorAll() {
        ReFactorExec reFactorExec = ReFactorExec.getInstance();
        reFactorExec.factorAll();
    }

    public void refactorOne(String javaName) {
        ReFactorExec reFactorExec = ReFactorExec.getInstance();
        reFactorExec.factorOne(javaName);
    }

    public String getJavaFileTree() {
        return JSON.toJSONString(Store.rootNode);
    }

    public JavaModelVo getJavaModelVo(String filePath) {
        System.out.println("asdasds"+Store.unitMaps.get(filePath));
        return Store.unitMaps.get(filePath);
    }


    public static void main(String[] args) {
        AnalysisApi analysisApi = AnalysisApi.getInstance();
        analysisApi.analysis("D:\\gitProject\\W8X");
        //System.out.println(JSON.toJSONString(Store.javaTree));
    }

}
