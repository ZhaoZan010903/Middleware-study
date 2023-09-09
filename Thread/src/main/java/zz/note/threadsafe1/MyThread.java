package zz.note.threadsafe1;

public class MyThread extends Thread {

    static int ticket = 0;

    @Override
    public void run() {
        while (true) {
            synchronized (MyThread.class) {
                if (ticket < 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ticket++;
                    System.out.println(getName() + "正在卖第" + ticket + "张票");
                } else {
                    break;
                }
            }
        }
    }
}
