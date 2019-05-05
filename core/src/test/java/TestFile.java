public class TestFile {

  public void tcp(){

  }

  double getPayAmount() {
    double result;
    if (IsDead()) {
      result = DeadAmount();
    }
    if (IsSeparated()) {
      result = SeparatedAmount();
    }
    else {
      if (IsRetired()) {
        result = RetiredPayAmount();
      } else {
        result = NormalPayAmount();
      }
    }
    return result;
  }


  double regetPayAmount() {
    double result;
    if (IsDead()) {
      result = DeadAmount();
      return result;
    }
    if (IsSeparated()) {
      result = SeparatedAmount();
      return result;
    }
    if (IsRetired()) {
      result = RetiredPayAmount();
      return result;
    }
    result = NormalPayAmount();
    return result;
  }


  private boolean t() {
    if (isNoEach()) {
      if (isHome()) {

      } else {
        return false;
      }
    } else {
      if (isHome()) {
        return false;
      } else {
        return true;
      }
    }
    /**
     * */
    return true;
  }


  private static boolean checkQQ(String qq) {
    int len = qq.length();

    if (len >= 5 && len <= 15) {
      if (!qq.startsWith("0")) {
        try {
          long num = Long.parseLong(qq);
        } catch (NumberFormatException e) {
          System.out.println(qq + "号含有字母");
          return false;
        }

      } else {
        System.out.println(qq + "号以0开头了");
        return false;
      }

    } else {
      System.out.println(qq + "号的长度不合法");
      return false;
    }
    return true;
  }


  public void cp() {
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

  public static void main(String args[]){
    TestFile testFile = new TestFile();
    testFile.cp();
    testFile.rcp();
  }

  public void rcp() {
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


  public void k() {
    int i = 0;
    if (i == 1) {
      System.out.println("执行1");
      if (i == 2) {
        System.out.println("执行2");
        if (i == 3) {
          System.out.println("执行3");
          if (i == 4) {
            System.out.println("执行4");
            if (i == 5) {
              System.out.println("执行5");
              if (i == 6) {
                System.out.println("执行6");
                if (i == 7) {
                  System.out.println("执行7");
                }
              }
            }
          }
        }
      }
    }
  }

  public void recfactoring() {
    int i = 0;
    if (i != 1) {
      return;
    }
    System.out.println("执行1");
    if (i != 2) {
      return;
    }
    System.out.println("执行2");
    if (i != 3) {
      return;
    }
    System.out.println("执行3");
    if (i != 4) {
      return;
    }
    System.out.println("执行4");
    if (i != 5) {
      return;
    }
    System.out.println("执行5");
    if (i != 6) {
      return;
    }
    System.out.println("执行6");
    if (i != 7) {
      return;
    }
    System.out.println("执行6");
  }

  private boolean isHome() {
    return true;
  }

  private boolean isWorlkDay() {
    return false;
  }

  private boolean isFlush() {
    return true;
  }

  private boolean isNoEach() {
    return false;
  }

  private boolean isDone() {
    return true;
  }

  private boolean isCooking() {
    return true;
  }

  private boolean isLate() {
    return true;
  }

  private double NormalPayAmount() {
    return 0;
  }

  private double RetiredPayAmount() {
    return 0;
  }

  private boolean IsRetired() {
    return false;
  }

  private double SeparatedAmount() {
    return 0;
  }

  private boolean IsSeparated() {
    return false;
  }

  private double DeadAmount() {
    return 0;
  }

  private boolean IsDead() {
    return true;
  }
}