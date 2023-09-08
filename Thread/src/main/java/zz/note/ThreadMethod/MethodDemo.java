package zz.note.ThreadMethod;

public class MethodDemo {
    /*
    String getName()   返回线程的名称
    void setName()      设置线程的名字(构造方法也可以设置名字)
        细节:
            1. 如果我们没有给线程设置名字, 线程也是有默认的名字的
                        格式: Thread -X (X序号1,从0开始的)
            2. 如果我们要给线程设置名字 , 可以用set方法进行设置 , 也可以构造方法设置

    static Thread currentThread()     获取当前线程的对象
        细节:
         当JVM虚拟机启动之后, 会自动的启动多条线程
         其中有一条线程就叫做main线程

    static void sleep(long time)           让线程休眠指定的时间,单位为毫秒
     */

    public static void main(String[] args) throws InterruptedException {

        // 1.创建线程的对象
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();

        // 2.开启线程
        t1.start();
        t2.start();

//         那条线程直行道这个方法,此时获取的就是哪条线程的对象
        Thread t = Thread.currentThread();
        String name = t.getName();
        Thread.sleep(2000);


        System.out.println(t);
    }
}
