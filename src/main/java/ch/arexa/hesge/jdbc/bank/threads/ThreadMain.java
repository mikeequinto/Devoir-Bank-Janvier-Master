package ch.arexa.hesge.jdbc.bank.threads;

public class ThreadMain {

    public static void main() {

        BankThread b = new BankThread();
        b.start();

    }

}
