package com.w8x.web.api;

import com.w8x.web.config.GitConfig;
import com.w8x.web.model.CommitMsg;
import com.w8x.web.model.Commits;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GithubDataGrabber {

  @Autowired
  GitConfig gitConfig;

  private static Logger LOGGER = LoggerFactory.getLogger(GithubDataGrabber.class);
  /**
   * 根据提交信息获取仓库信息并clone到本地
   *
   * @param CommitMsg 提交信息
   */
  public boolean gitCloneRepository(CommitMsg commitMsg) {
    String fullName = commitMsg.getRepository().getFull_name();
    String filePath = gitConfig.getWorkspace() + "/" + fullName;
    Git git = null;
    LOGGER.info("开始克隆仓库 仓库地址为:"+commitMsg.getRepository().getClone_url());
    try {
      git = Git.cloneRepository()
          .setDirectory(new File(filePath))
          .setURI(commitMsg.getRepository().getClone_url())
          .setBranch(commitMsg.getRef()).call();
    } catch (GitAPIException e) {
    LOGGER.info("克隆失败");
    e.printStackTrace();
    }
    if (git == null) {
      return false;
    }
    git.close();
    LOGGER.info("克隆成功");
    return true;
  }

  /**
   * 根据提交信息从远程仓库pull 文件
   *
   * @param CommitMsg 提交信息
   */
  public Boolean gitPullRepository(CommitMsg commitMsg) {
    String fullName = commitMsg.getRepository().getFull_name();
    String filePath = gitConfig.getWorkspace() + "/" + fullName;
    Git git = null;
    LOGGER.info("开始同步仓库 仓库地址为:"+commitMsg.getRepository().getClone_url());
    try {
      git = Git.open(new File(filePath));
      PullResult result = git.pull()
          .setRemote(commitMsg.getRepository().getPulls_url())
          .setRemoteBranchName(commitMsg.getRef())
          .call();
      if (!result.isSuccessful()) {
        return false;
      }
    } catch (GitAPIException e) {
      LOGGER.info("同步失败");
      e.printStackTrace();
    } catch (IOException e) {
      LOGGER.info("同步失败");
      e.printStackTrace();
    }
    if (git == null) {
      return false;
    }
    git.close();
    LOGGER.info("成功");
    return true;
  }

  /**
   * 切换到fix 分支并提交到仓库
   * push到远程仓库
   */
  public Boolean gitPushRepository(CommitMsg commitMsg) {
    String fullName = commitMsg.getRepository().getFull_name();
    String filePath = gitConfig.getWorkspace() + "/" + fullName;
    Git git = null;
    LOGGER.info("开始提交仓库 仓库地址为:"+commitMsg.getRepository().getClone_url());
    try {
      git = Git.open(new File(filePath));
      Ref ref = git.checkout().setCreateBranch(true).setName("fix").call();
      LOGGER.info("切换分支:"+ref.getName());
      commitLocalRepository(git,commitMsg);
      LOGGER.info("提交代码至本地成功");
      git.push().add(ref).call();
    } catch (GitAPIException e) {
      e.printStackTrace();
      LOGGER.info("提交代码失败");
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(git==null){
      return false;
    }
    git.close();
    LOGGER.info("提交代码至服务器成功");
    return true;
  }

  /**
   * 删除本地仓库
   */
  public Boolean deleteRepository(CommitMsg commitMsg) {
    String fullName = commitMsg.getRepository().getFull_name();
    String filePath = gitConfig.getWorkspace() + "/" + fullName;
    Git git = null;

    return true;
  }

  /**
   * 查看仓库是否已经在本地
   */
  public boolean checkLocalRepository(String fullName) {
    LOGGER.info("查询仓库是否存在:"+fullName);
    try{
      File file = new File(gitConfig.getWorkspace()+"/"+fullName);
      if(!file.exists()){
        return false;
      }
      Git git = Git.open(file);
      if(git==null){
        LOGGER.info("仓库不存在:"+fullName);
        return false;
      }
    }catch (IOException e){
      e.printStackTrace();
      LOGGER.info("查询失败:"+fullName);
    }
    LOGGER.info("仓库存在:"+fullName);
    return true;
  }

  /**
   * 提交到本地仓库
   */
  private void commitLocalRepository (Git git, CommitMsg commitMsg) throws GitAPIException {
    LOGGER.info("提交代码至本地");
    AddCommand add = git.add();
    add.addFilepattern(".").call();
    CommitCommand commit = git.commit();
    commit.setCommitter(gitConfig.getCommiter(),gitConfig.getMail());
    commit.setAuthor(gitConfig.getCommiter(),gitConfig.getMail());
    commit.setMessage(gitConfig.getMessage());
    commit.call();
  }

  /**
   * 整理提交的文件并生成一个列表
   */
  public List<String> collectJavaFile(CommitMsg commitMsg) {
    LOGGER.info("生成文件列表");
    Iterator<Commits> it = commitMsg.getCommits().iterator();
    List<String> add = null;
    List<String> modified = null;
    List<String> result = new ArrayList<>();
    while (it.hasNext()) {
      Commits commits = it.next();
      it.remove();
      add = commits.getAdded();
      modified = commits.getModified();
      add.addAll(modified);
      for(int i = 0;i<add.size();i++){
        add.set(i,gitConfig.getWorkspace()+"/"+commitMsg.getRepository()
            .getFull_name()+"/"+add.get(i));
      }
      result.addAll(add);
    }
    Iterator<String> fileIt = result.iterator();
    LOGGER.info("过滤文件");
    while(fileIt.hasNext()){
      String fileName = fileIt.next();
      if(filtter(fileName)!=1){
        fileIt.remove();
      }
    }
    return result;
  }

  private int filtter(String fileName){
    if(fileName.substring(fileName.lastIndexOf(".")+1).equals("java")){
      return 1;
    }
    return 0;
  }


}
