package analysis.process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定线程工厂
 * */
public class RuleThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger();
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(Integer.toString(counter.get()));
        counter.incrementAndGet();
        return thread;
    }
}
