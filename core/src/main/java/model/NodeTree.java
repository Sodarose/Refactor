package model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件树
 *
 * @author Administrator*/
@AllArgsConstructor
@Data
public class NodeTree {

    //名称
    @JSONField(name = "name")
    private String fileName;
    //完全路径
    //private String path;
    //其下子Tree
    @JSONField(name = "children")
    private List<NodeTree> files = new ArrayList<>();
    //是否是文件
    //AST树

    public NodeTree(){

    }

    public NodeTree(String path, boolean b) {
        this.fileName = path;
    }

}
