package self.ed;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import static self.ed.utils.ThreadUtils.sleep;

public class ForkJoinPoolTest {
    public static void main(String[] args) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        ArraySumTask task = new ArraySumTask(new int[]{1, 2, 3, 4, 5});
        int sum = commonPool.invoke(task);
        System.out.println("computed sum: " + sum);
        CharCodePrintAction action = new CharCodePrintAction(new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'});
        commonPool.execute(action);
        // TODO: is there any better way to wait for all RecursiveActions
        sleep(1000);
    }

    private static class ArraySumTask extends RecursiveTask<Integer> {
        private final int[] array;

        public ArraySumTask(int[] array) {
            this.array = array;
        }

        @Override
        protected Integer compute() {
            if (array.length > 2) {
                System.out.println("splitting task into sub-tasks");
                return ForkJoinTask
                        .invokeAll(asList(
                                new ArraySumTask(copyOfRange(array, 0, array.length / 2)),
                                new ArraySumTask(copyOfRange(array, array.length / 2, array.length))))
                        .stream()
                        .mapToInt(ForkJoinTask::join)
                        .sum();
            } else if (array.length == 2) {
                System.out.println("computing sum of " + array[0] + " and " + array[1]);
                return array[0] + array[1];
            } else {
                System.out.println("returning single value " + array[0]);
                return array[0];
            }
        }
    }

    private static class CharCodePrintAction extends RecursiveAction {
        private final char[] chars;

        public CharCodePrintAction(char[] chars) {
            this.chars = chars;
        }

        @Override
        protected void compute() {
            if (chars.length > 1) {
                System.out.println("splitting action into sub-actions");
                ForkJoinTask.invokeAll(asList(
                        new CharCodePrintAction(copyOfRange(chars, 0, chars.length / 2)),
                        new CharCodePrintAction(copyOfRange(chars, chars.length / 2, chars.length))));
            } else {
                System.out.println(chars[0] + " => " + (int) chars[0]);
            }
        }
    }
}
