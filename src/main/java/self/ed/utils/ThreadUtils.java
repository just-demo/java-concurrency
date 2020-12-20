package self.ed.utils;

public class ThreadUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void wait(Object monitor) {
        try {
            monitor.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
