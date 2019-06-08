import Scanner.T;

public class TTest implements T {

    public void test(int i) {

    }

    class Q implements T {
        public void test(int i) {

        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

class P implements T {
    public void test(int i) {

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

class X extends P {
    public void test(int i) {
        super.test(i);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

class Tcp extends AbstractTest implements T,Qt{
    @Override
    public void test(int i) {

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

