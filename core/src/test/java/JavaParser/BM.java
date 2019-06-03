package JavaParser;


import java.util.Random;

/**
 * @ProjectName StringPatternMatchAlgorithm
 * @PackageName com.buaa
 * @ClassName BM
 * @Description TODO
 * @Author 刘吉超
 * @Date 2016-05-26 22:26:08
 */
public class BM {
    /**
     * 利用坏字符规则计算移动位数
     */
    public static int badCharacter(String moduleString, char badChar,int badCharSuffix){
        return badCharSuffix - moduleString.lastIndexOf(badChar, badCharSuffix);
    }

    /**
     * 利用好后缀规则计算移动位数
     */
    public static int goodCharacter(String moduleString,int goodCharSuffix){
        int result = -1;
        // 模式串长度
        int moduleLength = moduleString.length();
        // 好字符数
        int goodCharNum = moduleLength -1 - goodCharSuffix;

        for(;goodCharNum > 0; goodCharNum--){
            String endSection = moduleString.substring(moduleLength - goodCharNum, moduleLength);
            String startSection = moduleString.substring(0, goodCharNum);
            if(startSection.equals(endSection)){
                result = moduleLength - goodCharNum;
            }
        }

        return result;
    }

    /**
     * BM匹配字符串
     *
     * @param originString 主串
     * @param moduleString 模式串
     * @return 若匹配成功，返回下标，否则返回-1
     */
    public static int match(String originString, String moduleString){
        // 主串
        if (originString == null || originString.length() <= 0) {
            return -1;
        }
        // 模式串
        if (moduleString == null || moduleString.length() <= 0) {
            return -1;
        }
        // 如果模式串的长度大于主串的长度，那么一定不匹配
        if (originString.length() < moduleString.length()) {
            return -1;
        }

        int moduleSuffix = moduleString.length() -1;
        int module_index = moduleSuffix;
        int origin_index = moduleSuffix;

        for(int ot = origin_index; origin_index < originString.length() && module_index >= 0;){
            char oc = originString.charAt(origin_index);
            char mc = moduleString.charAt(module_index);
            if(oc  == mc){
                origin_index--;
                module_index--;
            }else{
                // 坏字符规则
                int badMove = badCharacter(moduleString,oc,module_index);
                // 好字符规则
                int goodMove = goodCharacter(moduleString,module_index);
                // 下面两句代码可以这样理解，主串位置不动，模式串向右移动
                origin_index = ot + Math.max(badMove, goodMove);
                module_index = moduleSuffix;
                // ot就是中间变量
                ot = origin_index;
            }
        }

        if(module_index < 0){
            // 多减了一次
            return origin_index + 1;
        }

        return -1;
    }

    /**
     * 随机生成字符串
     *
     * @param length 表示生成字符串的长度
     * @return String
     */
    public static String generateString(int length) {
        String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder result = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(baseString.charAt(random.nextInt(baseString.length())));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        // 主串
//        String originString = generateString(10);
        String originString = "HERE IS A SIMPLE EXAMPLE";
        // 模式串
//        String moduleString = generateString(4);
        String moduleString = "EXAMPLE";
        // 坏字符规则表
//        int[] badCharacterArray = badCharacter(originString,moduleString);

        System.out.println("主串：" + originString);
        System.out.println("模式串：" + moduleString);


        int index = match(originString, moduleString);
        System.out.println("匹配的下标：" + index);
    }
}