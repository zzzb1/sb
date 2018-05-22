package cn.zb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zb Created in 9:58 PM 2018/5/22
 */
public class ThreadTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        threadTest();
        executorService();
    }

    private static void executorService() throws ExecutionException, InterruptedException {
        int maxSize = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(maxSize);
        List<Future> list = new ArrayList<Future>();

        for (int i = 0; i < 5; i++) {
            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future f = executorService.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        executorService.shutdown();

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }
    }

    private static void threadTest() {
        Thread t1 = new Thread(new Thr());

        Thread t2 = new Thread(new Thr());
        t1.start();
        t2.start();
        for (int i = 0 ; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + i);
        }
    }

    static class Thr implements Runnable{

        @Override
        public void run() {
            for (int i = 0 ; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + i);
            }
        }
    }

    static class MyCallable implements Callable<Object> {
        private String taskNum;

        MyCallable(String taskNum) {
            this.taskNum = taskNum;
        }

        public Object call() throws Exception {
            System.out.println(">>>" + taskNum + "任务启动");
            Date dateTmp1 = new Date();
            Thread.sleep(1000);
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            System.out.println(">>>" + taskNum + "任务终止");
            return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
        }
    }
}
