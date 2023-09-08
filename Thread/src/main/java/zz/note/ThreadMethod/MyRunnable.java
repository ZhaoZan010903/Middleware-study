package zz.note.ThreadMethod;

public class MyRunnable {
    public static void main(String[] args) {
        /*
        setPriority(int newPriority)  设置线程的优先级
        final int getPriority()       获取线程的优先级
         */


        Thread t1 = new MyThread();
        Thread t2 = new MyThread();

        t1.setPriority(1);
        t2.setPriority(10);

        t1.start();
        t2.start();


    }
}
