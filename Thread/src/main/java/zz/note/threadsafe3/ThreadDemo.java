package zz.note.threadsafe3;


public class ThreadDemo {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();
        MyThread t3 = new MyThread();

        t1.setName("第一个");
        t2.setName("第二个");
        t3.setName("第三个");


        t1.start();
        t2.start();
        t3.start();
    }
}
