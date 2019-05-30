package com.w8x.web.controller;

import com.w8x.web.Service.RefactCoreService;
import com.w8x.web.model.Code;
import com.w8x.web.model.CodeShown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/core")
public class WebRedux {

    @Autowired
    RefactCoreService refactCoreService;

    /**
     * 获得项目路径 并进行扫描
     */
    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ResponseBody
    public Code analysis(@RequestParam(value = "fileName") String fileName) {
        //System.out.println(fileName);
        Code code = refactCoreService.runAnalysis(fileName);
        return code;
    }

    @GetMapping("/projectTree")
    @ResponseBody
    public String javaFileTree() {
        return refactCoreService.getJavaFileTree();
    }

    @PostMapping("/javaFileDetail")
    @ResponseBody
    public CodeShown javaFileDetail(String filePath) {
        return refactCoreService.getJavaFileDetail(filePath);
    }

    @GetMapping("/refactorAll")
    @ResponseBody
    public Code refactorAll(){
        return Code.createCode(200,null,"success");
    }

    @PostMapping("/refactorOne")
    @ResponseBody
    public Code refactorOne(String filePath){
        return Code.createCode(200,null,"success");
    }

    @GetMapping("/undoAll")
    @ResponseBody
    public Code undoAll(){
        return Code.createCode(200,null,"success");
    }

    @PostMapping("/undoOne")
    @ResponseBody
    public Code undoOne(String filaName){
        return Code.createCode(200,null,"success");
    }

    @GetMapping("/saveCodeAll")
    @ResponseBody
    public Code saveCodeAll(){
        return Code.createCode(200,null,"success");
    }
}
