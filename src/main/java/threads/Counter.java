package threads;

/**
 * Created by john on 13.12.2018.
 */
public class Counter {

    private int value;
    private static Counter instance = new Counter(1);

    public static Counter getInstance(){
        return instance;
    }

    private Counter(int v){ this.value = v; }

    synchronized public void inc(){
        System.out.println(value++);
    }

}
