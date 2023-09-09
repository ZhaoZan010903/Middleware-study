package zz.note.threadsafe2;

public class ThreadDemo {
    public static void main(String[] args) {

        MyRunnable mr = new MyRunnable();


        Thread t1 = new Thread(mr);
        Thread t2 = new Thread(mr);
        Thread t3 = new Thread(mr);


        t1.setName("第一个");
        t2.setName("第二个");
        t3.setName("第三个");


        t1.start();
        t2.start();
        t3.start();
    }
}
