package zz.note.Runnable;

public class MyRunnable implements Runnable{
    @Override
    public void run() {
        //
        for (int i = 0; i < 100; i++) {
            Thread t = Thread.currentThread();
            System.out.println("Hello World"+t.getName());

        }
    }
}
