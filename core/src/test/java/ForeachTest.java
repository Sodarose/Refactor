import java.util.ArrayList;
import java.util.List;

public class ForeachTest {
    public void test1(){
        List<String> list = new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add(""+i);
        }
        for(String qps:list){
            run();
            run();
            list.remove(qps);
            run();
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
