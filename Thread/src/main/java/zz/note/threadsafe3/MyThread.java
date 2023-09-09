package zz.note.threadsafe3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThread extends Thread {

    static int ticket = 0;
//     ↓ 加static关键字 保证创建一个锁对象
    static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try{
                if (ticket == 100) {
                    break;
                } else {
                    Thread.sleep(100);
                    ticket++;
                    System.out.println(Thread.currentThread().getName() + "在卖第" + ticket + "张票!!!");
                }
            }catch (InterruptedException e){
                e.printStackTrace();
//           添加finally关键字,必须执行关闭锁的对象
            }finally {
                lock.unlock();
            }
        }
    }
}
