package zz.note.ThreadMethod;

public class MethodDemo1 {
    public static void main(String[] args) {
        /*
        守护线程
         */
        MyThread t1 = new MyThread();
        MyThread1 t2 = new MyThread1();

        t1.setName("女神");
        t2.setName("备胎");

        t2.setDaemon(true);

        t1.start();
        t2.start();
    }
}
