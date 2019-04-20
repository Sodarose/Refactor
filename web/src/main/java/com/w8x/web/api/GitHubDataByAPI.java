package com.w8x.web.api;

import com.w8x.web.config.GitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubDataByAPI {
  @Autowired
  GitConfig gitConfig;

  public void getCommitFile(){

  }
}
