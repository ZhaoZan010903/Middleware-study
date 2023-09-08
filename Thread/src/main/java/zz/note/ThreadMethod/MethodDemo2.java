package zz.note.ThreadMethod;

public class MethodDemo2 {
    public static void main(String[] args) throws InterruptedException {
        /*
        public final void join() 插入线程/插队线程
         */

        MyThread t = new MyThread();
        t.setName("土豆");
        t.start();

        //表示把t这个线程,插入到当前线程之前.

        t.join();

        for (int i = 0; i < 100; i++) {
            System.out.println("main线程" + i);
        }
    }
}
