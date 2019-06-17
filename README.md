# 简介
    系统的逻辑处理层主要通过javaparse库来进行生成抽象语法树，然后对抽象语法树进行分析和收集问题代码数据，
    最后发到对应的问题代码重构类进行重构。服务端用sprinboot来写的，导出jar即可运行。
#目录介绍
    core:代码处理模块
        |—————analysis代码分析模块
        |—————api接口
        |—————form
        |—————io 读取路径模块
        |—————model数据类模块
        |—————refactor代码重构模块
        |—————refer 引用重构模块
        |—————utils 工具模块
    web:web展示模块
        |—————Controller控制层（接收请求并处理）
        |—————Service服务层（处理请求的逻辑层）
        |—————Model数据类
        |—————WebApplication启动文件
     
#快速开始
java -jar xx.jar

## 

