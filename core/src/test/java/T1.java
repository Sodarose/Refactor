public class T1 {

  public static void main(String args[]){
    cp();
    rcp();
  }
  public static void rcp() {
    if (isWorlkDay()) {
      if (isFlush()) {
        System.out.println("刷卡成功");
        return;
      }
      if (isLate()) {
        System.out.println("迟到了");
        return;
      }
      if (isDone()) {
        System.out.println("死在路上了");
        return;
      }
      System.out.println("其他原因");
      return;
    }
    if (isHome()) {
      System.out.println("在家");
      return;
    }
    if (isNoEach()) {
      System.out.println("没吃早餐");
      return;
    }
    if (isCooking()) {
      System.out.println("做饭");
      return;
    }
    System.out.println("等着饿死");
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
    return false;
  }

  private static boolean isNoEach() {
    return false;
  }

  private static boolean isHome() {
    return false;
  }

  private static boolean isDone() {
    return false;
  }

  private static boolean isLate() {
    return false;
  }

  private static boolean isFlush() {
    return false;
  }

  private static boolean isWorlkDay() {
    return false;
  }
}
