package zz.note.Runnable;

import java.util.Arrays;

public class ThreadDemo {
    public static void main(String[] args) throws NoSuchFieldException {
        /**
         * 多线程的第二种启动方式
         *  1.自己定义一个实现Runnable接口
         *  2.重写里面的run方法
         *  3.创建自己的类的对象
         *  4.创建一个Thread类的对象,并开启线程
         */

        //创建MyRun的对象
        //表示多线程要执行的任务
        MyRunnable my = new MyRunnable();

        //创建线程对象
        Thread t1 = new Thread(my);
        Thread t2 = new Thread(my);

        //设置名字
        t1.setName("t1");
        t2.setName("t2");

        //开启多线程
//        t1.start();
//        t2.start();
    }
}
