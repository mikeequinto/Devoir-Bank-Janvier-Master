package ch.arexa.hesge.jdbc.bank.threads;

public class ThreadMain {

    public static void main(String arg[]) {

        BankThread b = new BankThread();
        b.start();

    }

}
