package com.jab.bankproject.dao;

import com.jab.bankproject.entities.BankAccount;
import com.jab.bankproject.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionDaoDB implements TransactionDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<Transaction> getAllTransactions() {
        final String GET_ALL_TRANSACTIONS = "SELECT * FROM transactions";
        return jdbc.query(GET_ALL_TRANSACTIONS, new TransactionMapper());
    }

    //not currently used, but would be useful if a page was added to view all transactions from a specific bank account.
    @Override
    public List<Transaction> getAllTransactionsFromAccountID(int accountID) {
        final String GET_TRANSACTIONS_FROM_ACCOUNT = "SELECT * FROM transactions WHERE fromaccountID = ?";
        return jdbc.query(GET_TRANSACTIONS_FROM_ACCOUNT, new TransactionMapper(), accountID);
    }

    //not currently used, but would be useful if a page was added to view all transactions to a specific bank account.
    @Override
    public List<Transaction> getAllTransactionsToAccountID(int accountID) {
        final String GET_TRANSACTIONS_TO_ACCOUNT = "SELECT * FROM transactions WHERE toaccountID = ?";
        return jdbc.query(GET_TRANSACTIONS_TO_ACCOUNT, new TransactionMapper(), accountID);
    }

    @Override
    public List<Transaction> getTransactionsByUserID(int userID) {
        final String GET_TRANSACTIONS_BY_USER = "SELECT * FROM transactions WHERE userID = ?";
        return jdbc.query(GET_TRANSACTIONS_BY_USER, new TransactionMapper(), userID);
    }

    @Override
    public Transaction getTransactionByID(int id) {
        try {
            final String GET_TRANSACTION_BY_ID = "SELECT * FROM transactions WHERE transactionID = ?";
            return jdbc.queryForObject(GET_TRANSACTION_BY_ID, new TransactionMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        final String INSERT_TRANSACTION = "INSERT INTO transactions" +
                "(userID, fromaccountID, toaccountID, transferamount, transferdate) " +
                "VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_TRANSACTION,
                transaction.getUserID(),
                transaction.getFromBankAccountID(),
                transaction.getToBankAccountID(),
                transaction.getTransferAmount(),
                transaction.getTransferDate());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        transaction.setId(newId);
        return transaction;
    }

    @Override
    public void deleteTransactionByID(int id) {
        final String DELETE_TRANSACTION = "DELETE FROM transactions WHERE transactionID = ?";
        jdbc.update(DELETE_TRANSACTION, id);
    }

    protected static final class TransactionMapper implements RowMapper<Transaction> {

        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transaction transaction = new Transaction(
                    rs.getInt("transactionID"),
                    rs.getInt("userID"),
                    rs.getInt("fromaccountID"),
                    rs.getInt("toaccountID"),
                    rs.getFloat("transferamount"),
                    rs.getDate("transferdate")
            );
            return transaction;
        }
    }
}
