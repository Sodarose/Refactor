package com.w8x.web;

import com.w8x.web.Service.WebHookService;
import com.w8x.web.config.GitConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

  @Autowired
  WebHookService webHookService;

  @Test
  public void contextLoads() throws IOException {
      String json = new String(Files.readAllBytes(Paths.get("/home/kangkang/"
          + "IdeaProjects/w8x/web/src/main/resources/static/push.json")),"UTF-8");
      webHookService.webHookSolve(json);
  }

}
