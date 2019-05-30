package com.w8x.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class WebApplication {

  public static void main(String[] args) {
    ApplicationContext ctx=SpringApplication.run(WebApplication.class, args);
  }


}
