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
        TransferOrder to = new TransferOrder();to.setAccountRef1(a.getAccountRef());to.setAccountRef2(a2.getAccountRef());
                            to.setAmount(10.0);to.setExecutionDate(date);to.setOperationDate(date);to.setStatus(Status.OPEN);
        to.persist();
    }

    @Test
    public void transferTestAlimentation() throws SQLException, java.text.ParseException{

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse("09/01/2019");
        Date date2 = dateFormat.parse("09/01/2019");
        Date date3 = dateFormat.parse("09/01/2019");
        Date date4 = dateFormat.parse("09/01/2019");
        Date date5 = dateFormat.parse("09/01/2019");
        Date date6 = dateFormat.parse("09/01/2019");
        Date date7 = dateFormat.parse("10/01/2019");
        Date date8 = dateFormat.parse("10/01/2019");
        Date date9 = dateFormat.parse("10/01/2019");
        Date date10 = dateFormat.parse("10/01/2019");


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
        TransferOrder to1 = new TransferOrder();to1.setAccountRef1(a.getAccountRef());to1.setAccountRef2(a2.getAccountRef());
                            to1.setAmount(10.0);to1.setExecutionDate(date1);to1.setOperationDate(date);to1.setStatus(Status.OPEN);
        TransferOrder to2 = new TransferOrder();to2.setAccountRef1(a.getAccountRef());to2.setAccountRef2(a2.getAccountRef());
                            to2.setAmount(10.0);to2.setExecutionDate(date2);to2.setOperationDate(date);to2.setStatus(Status.OPEN);
        TransferOrder to3 = new TransferOrder();to3.setAccountRef1(a.getAccountRef());to3.setAccountRef2(a2.getAccountRef());
                            to3.setAmount(10.0);to3.setExecutionDate(date3);to3.setOperationDate(date);to3.setStatus(Status.OPEN);
        TransferOrder to4 = new TransferOrder();to4.setAccountRef1(a.getAccountRef());to4.setAccountRef2(a2.getAccountRef());
                            to4.setAmount(10.0);to4.setExecutionDate(date4);to4.setOperationDate(date);to4.setStatus(Status.OPEN);
        TransferOrder to5 = new TransferOrder();to5.setAccountRef1(a.getAccountRef());to5.setAccountRef2(a2.getAccountRef());
                            to5.setAmount(10.0);to5.setExecutionDate(date5);to5.setOperationDate(date);to5.setStatus(Status.OPEN);
        TransferOrder to6 = new TransferOrder();to6.setAccountRef1(a.getAccountRef());to6.setAccountRef2(a2.getAccountRef());
                            to6.setAmount(10.0);to6.setExecutionDate(date6);to6.setOperationDate(date);to6.setStatus(Status.OPEN);
        TransferOrder to7 = new TransferOrder();to7.setAccountRef1(a.getAccountRef());to7.setAccountRef2(a2.getAccountRef());
                            to7.setAmount(10.0);to7.setExecutionDate(date7);to7.setOperationDate(date);to7.setStatus(Status.OPEN);
        TransferOrder to8 = new TransferOrder();to8.setAccountRef1(a.getAccountRef());to8.setAccountRef2(a2.getAccountRef());
                            to8.setAmount(10.0);to8.setExecutionDate(date8);to8.setOperationDate(date);to8.setStatus(Status.OPEN);
        TransferOrder to9 = new TransferOrder();to9.setAccountRef1(a.getAccountRef());to9.setAccountRef2(a2.getAccountRef());
                            to9.setAmount(10.0);to9.setExecutionDate(date9);to9.setOperationDate(date);to9.setStatus(Status.OPEN);
        TransferOrder to10 = new TransferOrder();to10.setAccountRef1(a.getAccountRef());to10.setAccountRef2(a2.getAccountRef());
                            to10.setAmount(10.0);to10.setExecutionDate(date10);to10.setOperationDate(date);to10.setStatus(Status.OPEN);

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
        TransferOrder to1 = new TransferOrder();to1.setAccountRef1(a.getAccountRef());to1.setAccountRef2(a2.getAccountRef());
                            to1.setAmount(10.0);to1.setExecutionDate(date1);to1.setOperationDate(date);to1.setStatus(Status.OPEN);
        to1.persist();

        //to1.setStatus(Status.CLOSED);
        //to1.update();
    }

}