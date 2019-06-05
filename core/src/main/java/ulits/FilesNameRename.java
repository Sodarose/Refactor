package ulits;

import model.Issue;
import model.JavaModel;

import java.io.File;

public class FilesNameRename {
    public static void nameRename(Issue issue,String oldFileName,String newFileName){
        JavaModel javaModel=issue.getJavaModel();
        String path=javaModel.getReadPath();
        File file=new File(path);

        File parentPath=file.getParentFile();
        File newFile=new File(parentPath+"/"+newFileName);
        file.renameTo(newFile);
        System.out.println(oldFileName);
        System.out.println("重命名成功");
    }
}
