package ulits;

import model.Issue;
import model.JavaModel;
import model.Store;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirNameRename {
    public static void nameRename(Issue issue,String oldPackageName,String newPackageName){
       String[] oldName=oldPackageName.split(".");
       String[] newName=newPackageName.split(".");
       oldPackageName="";
       newPackageName="";
       for (int i=0;i<oldName.length;i++){
           if(oldPackageName.equals("")) {
               oldPackageName = oldPackageName + oldName[i];
           }
           else {
               oldPackageName=oldPackageName+"\\"+newName[i];
           }
       }
       for (int i=0;i<newName.length;i++){
           if(newPackageName.equals("")) {
               newPackageName = newPackageName + newName[i];
           }else {
               newPackageName=newPackageName+"\\"+newName[i];
           }
       }
        System.out.println(oldPackageName);
        System.out.println(newPackageName);
        JavaModel javaModel=issue.getJavaModel();
        String path=javaModel.getReadPath();
        File file=new File(path);
        String parentPath=file.getParent();
        String newParentPath=parentPath.replaceAll(oldPackageName,newPackageName);
        System.out.println(newParentPath);
        renameDirectory(parentPath, newParentPath);
        Map<String,JavaModel> modelMap=Store.javaModelMap;
        List<String> fileList=readFildName(parentPath);
        for (String filePath:fileList){
            String newFilePath=filePath.replaceAll(oldPackageName,newPackageName);
            JavaModel model=modelMap.get(filePath);
            model.setReadPath(newFilePath);
            modelMap.remove(filePath);
            modelMap.put(newFilePath,javaModel);
        }
    }
    public static List<String> readFildName(String path){
        List<String> fileList=new ArrayList<>();
        Map<String,JavaModel> javaModelMap= Store.javaModelMap;
        for (String file:javaModelMap.keySet()){
            BoyerMoore boyerMoore=new BoyerMoore();
            int pos=boyerMoore.match(file,path);
            if(pos!=-1){
                fileList.add(file);
            }
        }
        return fileList;
    }
    public static void renameDirectory(String fromDir,String toDir){
        File from=new File(fromDir);
        if(!from.exists()||from.isDirectory()){
            return;
        }
        File to =new File(toDir);
        from.renameTo(to);
        System.out.println("重命名文件夹");
    }
    /*
    public static void main(String[] args){
        nameRename("C:\\Users\\Administrator\\Desktop\\MyBlog");
    }
    */
}
