package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Account;
import ch.arexa.hesge.jdbc.bank.Event;
import ch.arexa.hesge.jdbc.bank.TransferOrder;
import ch.arexa.hesge.jdbc.bank.exceptions.AmountNotAvailableException;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static ch.arexa.hesge.jdbc.bank.helper.DateHelper.toSqlDate;

/**
 * Created by john on 08.12.2018.
 */
public class TransferOrderDA extends BankDA {

    AccountDA accountDao = new AccountDA();

    public Long persist(TransferOrder e) throws SQLException {

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("insert into TransferOrder(accountRefSource, accountRefTarget, amount, executionDate, operationDate, status) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, e.getAccountRef1());
            pStmt.setString(2, e.getAccountRef2());
            pStmt.setDouble(3, e.getAmount());
            pStmt.setDate(4, new java.sql.Date(e.getExecutionDate().getTime()));
            pStmt.setDate(5, new java.sql.Date(e.getOperationDate().getTime()));
            pStmt.setString(6, "OPEN");
            pStmt.executeUpdate();
            rs = pStmt.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                return id;
            } else {
                throw new SQLException("Failed to persist entity ");
            }
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

    public void update(TransferOrder to) throws SQLException{

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("update transferorder set idTransferOrder=?, accountRefSource=?, accountRefTarget=?, amount=?, executionDate=?, operationDate=?, status=? where idTransferOrder=?");
            pStmt.setLong(1, to.getIdTransferOrder());
            pStmt.setString(2, to.getAccountRef1());
            pStmt.setString(3, to.getAccountRef2());
            pStmt.setDouble(4, to.getAmount());
            pStmt.setDate(5, toSqlDate(to.getExecutionDate()));
            pStmt.setDate(6, toSqlDate(to.getOperationDate()));
            pStmt.setString(7, to.getStatus().toString());
            pStmt.setLong(8, to.getIdTransferOrder());

            pStmt.executeUpdate();
        } finally {
            closeAll(con, pStmt, rs);
        }

    }


    public void updateAccounts(String accountRef1, String accountRef2, Date executionDate, double amount) throws SQLException, AmountNotAvailableException{

        Account a;
        Account a2;
        try{

            a = accountDao.findAccountByRef(accountRef1);
            a2 = accountDao.findAccountByRef(accountRef2);

            if(a.getBalance() - amount < 0){
                throw new AmountNotAvailableException("Amount not available on source account!");
            }
            a.setBalance(a.getBalance() - amount);
            a2.setBalance(a2.getBalance() + amount);
            a.update();
            a2.update();

            Event e1 = new Event(a.getAccountRef(), executionDate, amount, "Debit");
            Event e2 = new Event(a2.getAccountRef(), executionDate, amount, "Credit");
            e1.persist();
            e2.persist();

        }catch(ObjectNotFoundException o){
            System.out.println(o);
        }

    }

    public List<TransferOrder> getTransferOrdersByDate() throws SQLException{

        List<TransferOrder> list = new ArrayList();

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {

            con = getConnection();
            pStmt = con.prepareStatement("select * from transferorder where operationDate = CURDATE() order by operationDate asc");
            rs = pStmt.executeQuery();
            while (rs.next()) {

                TransferOrder to = new TransferOrder(rs.getString("accountRefSource"), rs.getString("accountRefTarget"), rs.getDouble("amount"), rs.getDate("executionDate"), rs.getDate("operationDate"));
                to.setIdTransferOrder(rs.getLong("idTransferOrder"));
                list.add(to);

            }
            return list;
        } finally {
            closeAll(con, pStmt, rs);
        }

    }

    public void checkTransferOrders() throws SQLException, AmountNotAvailableException{

        List<TransferOrder> list = getTransferOrdersByDate();

        Date currentDate = new Date();

        for(TransferOrder to : list){
            if(to.getExecutionDate() == currentDate && to.getStatus() == Status.OPEN){
                updateAccounts(to.getAccountRef1(), to.getAccountRef2(), to.getExecutionDate(), to.getAmount());
                to.setStatus(Status.CLOSED);
            }
        }

    }

}
