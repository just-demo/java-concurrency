package self.ed;

import self.ed.utils.ThreadUtils;

import java.util.concurrent.CountDownLatch;

import static self.ed.utils.LogUtils.log;
import static self.ed.utils.ThreadUtils.sleep;


public class WaitNotifyTest {
    public static void main(String[] args) throws InterruptedException {
        log("Start");
        int waitingThreads = 2;
        CountDownLatch latch = new CountDownLatch(waitingThreads);
        Object monitor = new Object();
        for (int i = 0; i < waitingThreads; i++) {
            new Thread(() -> {
                synchronized (monitor) {
                    log("Before wait");
                    ThreadUtils.wait(monitor);
                    log("After wait");
                    latch.countDown();
                }
            }).start();
        }
        sleep(1000);
        synchronized (monitor) {
            log("Before notifyAll");
            monitor.notifyAll(); // wait() would wake up only one waiting thread
            log("After notifyAll");
        }
        latch.await();
        log("End");
    }
}
