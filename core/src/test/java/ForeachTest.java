import java.util.ArrayList;
import java.util.List;

public class ForeachTest {
    List<String> list = new ArrayList<>();
    public void test1(){
        for(String qps:list){
            run();
            list.remove(qps);
            qie(list);
        }
    }
    public void run(){

    }

    public void qie(List<String> list){
        list.remove(10);
        pqe(list);
    }

    public void pqe(List<String> list){
        list.remove(100);
    }
}
