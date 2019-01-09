package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Greetings extends Thread {


    Counter c = Counter.getInstance();
    int nb = 0;

    public void run(){

        while(nb < 10){
            c.inc();
            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            nb++;
        }
    }

}
