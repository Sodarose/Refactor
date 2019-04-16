package com.w8x.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class t {
  @GetMapping("/index")
  public String getIndex(){
    return "index.html";
  }
}
