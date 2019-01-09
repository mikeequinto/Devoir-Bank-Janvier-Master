package ch.arexa.hesge.jdbc.bank.threads;

import ch.arexa.hesge.jdbc.bank.dao.TransferOrderDA;

import java.sql.SQLException;

public class BankThread extends Thread{

    TransferOrderDA transferDAO = new TransferOrderDA();

    public void run(){

        while(true){

            try{
                transferDAO.checkTransferOrders();
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

}
