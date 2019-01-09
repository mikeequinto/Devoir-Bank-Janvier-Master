package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Salutation extends Thread {

    Counter c = Counter.getInstance();

    public void run(){

        while(true){

            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
