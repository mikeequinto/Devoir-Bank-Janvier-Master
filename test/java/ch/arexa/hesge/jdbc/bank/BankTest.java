package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.*;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//u

public class BankTest extends DBTest {

    private static final Logger logger = Logger.getLogger(BankDA.class);
    AccountDA accountDAO = new AccountDA();
    ClientDA clientDAO = new ClientDA();
    TransferOrderDA transferDAO = new TransferOrderDA();

    Client clientTest = new Client("michael", "allée des erables");
    Account accountTest = new Account(UUID.randomUUID().toString(), 1500.0, "CHF");

    @Before
    public void init() throws Exception {
        Connection con = accountDAO.getConnection();
        //reinitMySQL(con);
        accountDAO.closeAll(con,null,null);
    }

    @Test
    public void persistClientTest() throws Exception, ObjectNotFoundException {
        clientTest.persist();
        Client c = clientDAO.findClientById(clientTest.getId());
        Assert.assertEquals(c, clientTest);

        accountTest.setIdClient(clientTest.getId());
        accountTest.persist();
        Account a = accountDAO.findAccountByRef(accountTest.getAccountRef());
        Assert.assertEquals(a, accountTest);
    }

    @Test
    public void persistCient() throws Exception, ObjectNotFoundException {
        Client pierre = new Client("pierre", "pierre@gmail.com");
        pierre.persist();
        Client c = clientDAO.findClientById(pierre.getId());
        Assert.assertEquals(c, pierre);
        pierre.setAddress("rue du lac");
        pierre.update();
    }

    @Test
    public void insertClient() throws Exception, ObjectNotFoundException{

        Client john5 = new Client("john5", "3 place de la mairie");
        Client test2 = new Client("test2", "4 rue du rhone");
        john5.persist();
        test2.persist();
        Client c = clientDAO.findClientById(john5.getId());
        Assert.assertEquals(c, john5);
        Client c1 = clientDAO.findClientById(test2.getId());
        Assert.assertEquals(c1, test2);
    }

    @Test
    public void createAccount() {

        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        Account a = new Account(UUID.randomUUID().toString(), 0.0, "CHF");
        a.setIdClient(id);
        a.persist();

    }

    @Test
    public void addAccountToClient() throws Exception, ObjectNotFoundException {
        Client pascal = new Client("pascal5", "pascal@gmail.com");
        pascal.persist();
        Account acc1 = new Account(UUID.randomUUID().toString(), Double.valueOf(100), "CHF");
        acc1.setIdClient(pascal.getId());
        acc1.persist();


        Account account = accountDAO.findAccountByRef(acc1.getAccountRef());
        Assert.assertEquals(acc1, account);

        acc1.setCurrency("EUR");
        acc1.update();
        account = accountDAO.findAccountByRef(acc1.getAccountRef());
        Assert.assertEquals("EUR", account.getCurrency());


        List<Account> accounts = accountDAO.findAccountsByClientId(pascal.getId());
        Assert.assertEquals(1, accounts.size());
        Assert.assertTrue(accounts.contains(acc1));


        Account acc2 = new Account(UUID.randomUUID().toString(), Double.valueOf(200), "EUR");
        acc2.setIdClient(pascal.getId());
        acc2.persist();

        accounts = accountDAO.findAccountsByClientId(pascal.getId());
        Assert.assertEquals(2, accounts.size());
        Assert.assertTrue(accounts.contains(acc1));
        Assert.assertTrue(accounts.contains(acc2));

    }


    @Test
    public void findByCriteria() throws Exception, ObjectNotFoundException {
        Client pascal = new Client("pascal", "pascal@gmail.com");
        pascal.persist();
        Account acc1 = new Account(UUID.randomUUID().toString(), Double.valueOf(100), "CHF");
        acc1.setIdClient(pascal.getId());
        acc1.persist();

        AccountCriteria criteria= new AccountCriteria();
        criteria.setAccountRef(acc1.getAccountRef());
        criteria.setCurrency("EUR");
        criteria.setBalance(Double.valueOf(200));
        List<Account> accounts = accountDAO.findAccounts(criteria);
        Assert.assertEquals(0, accounts.size());
        
        criteria= new AccountCriteria();
        criteria.setAccountRef(acc1.getAccountRef());
        criteria.setCurrency("CHF");
        criteria.setBalance(Double.valueOf(100));
        accounts = accountDAO.findAccounts(criteria);
        Assert.assertEquals(1, accounts.size());
        acc1=accounts.get(0);
        Assert.assertEquals("CHF", acc1.getCurrency());
        Assert.assertEquals(Double.valueOf(100), acc1.getBalance(),0.01);

    }

    @Test
    public void transferTest() throws SQLException{

        //Client 1
        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        //Account 1
        Account a = new Account(UUID.randomUUID().toString(), 500.0, "CHF");
        a.setIdClient(id);
        a.persist();
        //Client 2
        Client c2 = new Client("test2", "4 rue du rhone");
        c2.persist();
        long id2 = c2.getId();
        //Account 2
        Account a2 = new Account(UUID.randomUUID().toString(), 100.0, "CHF");
        a2.setIdClient(id2);
        a2.persist();

        //TransferOrder
        Date date = new Date();
        TransferOrder to = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 100.0, date, date);
        to.persist();
    }

    @Test
    public void transferTestAlimentation() throws SQLException, java.text.ParseException{

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date operationDate = dateFormat.parse("06/01/2019 17:20:00");
        Date date1 = dateFormat.parse("06/01/2019 17:20:00");
        Date date2 = dateFormat.parse("06/01/2019 17:20:00");
        Date date3 = dateFormat.parse("06/01/2019 17:20:00");
        Date date4 = dateFormat.parse("06/01/2019 17:20:00");
        Date date5 = dateFormat.parse("06/01/2019 17:20:00");
        Date date6 = dateFormat.parse("06/01/2019 17:20:00");
        Date date7 = dateFormat.parse("06/01/2019 17:20:00");
        Date date8 = dateFormat.parse("06/01/2019 17:20:00");
        Date date9 = dateFormat.parse("06/01/2019 17:20:00");
        Date date10 = dateFormat.parse("06/01/2019 17:20:00");


        //Client 1
        Client c = new Client("test1", "10 passage de la fin");
        c.persist();
        long id = c.getId();
        //Account 1
        Account a = new Account(UUID.randomUUID().toString(), 5000.0, "CHF");
        a.setIdClient(id);
        a.persist();
        //Client 2
        Client c2 = new Client("test2", "4 rue du rhone");
        c2.persist();
        long id2 = c2.getId();
        //Account 2
        Account a2 = new Account(UUID.randomUUID().toString(), 3500.0, "CHF");
        a2.setIdClient(id2);
        a2.persist();

        //TransferOrder
        Date date = new Date();
        TransferOrder to1 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date1, operationDate);
        TransferOrder to2 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date2, operationDate);
        TransferOrder to3 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date3, operationDate);
        TransferOrder to4 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date4, operationDate);
        TransferOrder to5 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date5, operationDate);
        TransferOrder to6 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date6, operationDate);
        TransferOrder to7 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date7, operationDate);
        TransferOrder to8 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date8, operationDate);
        TransferOrder to9 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date9, operationDate);
        TransferOrder to10 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date10, operationDate);

        to1.persist();
        to2.persist();
        to3.persist();
        to4.persist();
        to5.persist();
        to6.persist();
        to7.persist();
        to8.persist();
        to9.persist();
        to10.persist();

        List<TransferOrder> listTransferOrders = transferDAO.getTransferOrdersByDate();

        Assert.assertEquals(10, listTransferOrders.size());

    }

    @Test
    public void updateStatus()throws SQLException, ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse("07/01/2019");

        //Client 1
        Client c = new Client("test1", "10 passage de la fin");
        c.persist();
        long id = c.getId();
        //Account 1
        Account a = new Account(UUID.randomUUID().toString(), 5000.0, "CHF");
        a.setIdClient(id);
        a.persist();
        //Client 2
        Client c2 = new Client("test2", "4 rue du rhone");
        c2.persist();
        long id2 = c2.getId();
        //Account 2
        Account a2 = new Account(UUID.randomUUID().toString(), 3500.0, "CHF");
        a2.setIdClient(id2);
        a2.persist();

        //TransferOrder
        Date date = new Date();
        TransferOrder to1 = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 10.0, date1, date);
        to1.persist();

        //to1.setStatus(Status.CLOSED);
        //to1.update();
    }

}