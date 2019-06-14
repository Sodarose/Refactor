public class Sample {


    public static int s(int i) {
        return 0;
    }

    double regetPayAmount() {
        int i = 0;
        double result = 0;
        while (i <= -1) {
            if (IsDead()) {
                result = DeadAmount();
                if (i == 1) {
                    System.out.println(1);
                    if (i == 2) {
                        System.out.println(2);
                        if (i == 3) {
                            System.out.println(3);
                            if (i == 4) {
                                System.out.println(4);
                                if (i == 5) {
                                    System.out.println(5);
                                    if (i == 6) {
                                        System.out.println(6);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (IsSeparated()) {
                    result = SeparatedAmount();
                } else {
                    if (IsRetired()) {
                        result = RetiredPayAmount();
                        if (i == 1) {
                            System.out.println(1);
                            if (i == 2) {
                                System.out.println(2);
                                if (i == 3) {
                                    System.out.println(3);
                                    if (i == 4) {
                                        System.out.println(4);
                                        if (i == 5) {
                                            System.out.println(5);
                                            if (i == 6) {
                                                System.out.println(6);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        result = NormalPayAmount();
                    }

                }
            }
        }
        return result;
    }

   /* public double cpp() {
        int i = 0;
        double result;
        if (IsDead()) {
            result = DeadAmount();
        } else {
            if (IsSeparated()) {
                result = SeparatedAmount();
            } else {
                if (IsRetired()) {
                    result = RetiredPayAmount();
                } else {
                    result = NormalPayAmount();
                }
            }
        }
        return result;
    }

    public double qqt() {
        int i = 0;
        double result;
        if (IsDead()) {
            result = DeadAmount();
            if (i != 1) {
                return result;
            }
            System.out.println(1);
            if (i != 2) {
                return result;
            }
            System.out.println(2);
            if (i != 3) {
                return result;
            }
            System.out.println(3);
            if (i != 4) {
                return result;
            }
            System.out.println(4);
            if (i != 5) {
                return result;
            }
            System.out.println(5);
            if (i != 6) {
                return result;
            }
            System.out.println(6);
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
    }*/

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
        return false;
    }


    public static void main(String[] args) {
        Sample sample = new Sample();
    }

}
