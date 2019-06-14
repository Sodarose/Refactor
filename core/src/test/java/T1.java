public class T1 {

    public static void main(String args[]) {
        cp();
        rcp();
    }

    public static void rcp() {
        int i = 1;
        if (i == 1) {
            if (i == 10) {
                if (i == 100) {
                    if (i == 1000) {
                        if (i == 200) {
                            if (i == 500) {
                                System.out.println("test");
                            } else {
                                System.out.println("娃哈哈");
                            }
                        }
                    }
                }
            }
            if (i == 10) {
                if (i == 100) {
                    if (i == 1000) {
                        if (i == 200) {
                            if (i == 500) {
                                System.out.println("test");
                            } else {
                                System.out.println("娃哈哈");
                            }
                        }
                    }
                }
            }
        } else if (i == 2) {
            System.out.println("ok");
        } else {
            if (i == 600) {
                System.out.println("你好啊");
            } else {
                if (i == 201) {
                    System.out.println("6666");
                }
            }
        }
    }


    public static void cp() {
        if (isWorlkDay()) {
            if (isFlush()) {
                System.out.println("刷卡成功");
            } else {
                if (isLate()) {
                    System.out.println("迟到了");
                } else {
                    if (isDone()) {
                        System.out.println("死在路上了");
                    } else {
                        System.out.println("其他原因");
                    }
                }
            }
        } else {
            if (isHome()) {
                System.out.println("在家");
            } else {
                if (isNoEach()) {
                    System.out.println("没吃早餐");
                } else {
                    if (isCooking()) {
                        System.out.println("做饭");
                    } else {
                        System.out.println("等着饿死");
                    }
                }
            }
        }
    }

    private static boolean isCooking() {
        return true;
    }

    private static boolean isNoEach() {
        return true;
    }

    private static boolean isHome() {
        return true;
    }

    private static boolean isDone() {
        return true;
    }

    private static boolean isLate() {
        return true;
    }

    private static boolean isFlush() {
        return true;
    }

    private static boolean isWorlkDay() {
        return true;
    }
}
