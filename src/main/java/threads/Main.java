package threads;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 13.12.2018.
 */
public class Main {

    public static void main(String[] args) {

        /*Counter counter = new Counter();

        Greetings greetings = new Greetings(counter);
        greetings.start();

        Salutation salutation = new Salutation(counter);
        salutation.start();*/

        /*ExecutorService executorService = Executors.newFixedThreadPool(5);
        Salutation salutation = new Salutation();
        Greetings greetings = new Greetings();
        executorService.execute(greetings);
        executorService.execute(salutation);

        ok wtf

        */

        List<Greetings> lst = new ArrayList();

        for(int i = 0; i < 10; i++){
            lst.add(new Greetings());
        }

        for(Greetings greeting : lst){
            greeting.start();
        }

    }

}
