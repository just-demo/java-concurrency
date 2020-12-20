package self.ed;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static self.ed.utils.ThreadUtils.sleep;
import static self.ed.utils.LogUtils.log;

public class ReadWriteLockTest {
    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
        Lock readLock = readWriteLock.readLock();
        Lock writeLock = readWriteLock.writeLock();

        new Thread(() -> {
            sleep(500);
            log("before writeLock.lock");
            writeLock.lock();
            log("after writeLock.lock");
            sleep(1000);
            log("before writeLock.unlock");
            writeLock.unlock();
            log("after writeLock.unlock");
        }).start();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                log("before readLock.lock");
                readLock.lock(); // unlike lock(), tryLock() would not honor waiting write lock
                log("after readLock.lock");
                sleep(1000);
                log("before readLock.unlock");
                readLock.unlock();
                log("after readLock.unlock");
            }).start();
            sleep(500);
        }
    }
}
