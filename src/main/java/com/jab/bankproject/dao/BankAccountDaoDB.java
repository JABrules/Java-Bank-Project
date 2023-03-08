package com.jab.bankproject.dao;

import com.jab.bankproject.entities.BankAccount;
import com.jab.bankproject.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BankAccountDaoDB implements BankAccountDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public List<BankAccount> getAllBankAccounts() {
        final String GET_ALL_ACCOUNTS = "SELECT * FROM bankaccounts";
        return jdbc.query(GET_ALL_ACCOUNTS, new BankAccountMapper());
    }

    @Override
    public List<BankAccount> getBankAccountsByUserID(int userID) {
        final String GET_ACCOUNTS_BY_USER = "SELECT * FROM bankaccounts WHERE userID = ?";
        return jdbc.query(GET_ACCOUNTS_BY_USER, new BankAccountMapper(), userID);
    }

    @Override
    public BankAccount getBankAccountByID(int id) {
        try {
            final String GET_ACCOUNT_BY_ID = "SELECT * FROM bankaccounts WHERE accountID = ?";
            return jdbc.queryForObject(GET_ACCOUNT_BY_ID, new BankAccountMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public BankAccount addBankAccount(BankAccount bankAccount) {
        final String INSERT_ACCOUNT = "INSERT INTO bankaccounts(userID, accountname, accountbalance) " +
                "VALUES(?,?,?)";
        jdbc.update(INSERT_ACCOUNT,
                bankAccount.getUserID(),
                bankAccount.getAccountName(),
                bankAccount.getAccountBalance());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        bankAccount.setId(newId);
        return bankAccount;
    }

    @Override
    public void updateFunds(float newFunds, int id){
        final String UPDATE_FUNDS = "UPDATE bankaccounts SET accountbalance = ? WHERE accountID = ?";
        jdbc.update(UPDATE_FUNDS, newFunds, id);
    }

    @Override
    @Transactional
    public void deleteBankAccountByID(int id) {
        final String DELETE_TRANSACTIONS = "DELETE FROM transactions WHERE fromaccountID = ? OR toaccountID = ?";
        jdbc.update(DELETE_TRANSACTIONS, id, id);

        final String DELETE_ACCOUNT = "DELETE FROM bankaccounts WHERE accountID = ?";
        jdbc.update(DELETE_ACCOUNT, id);
    }

    protected static final class BankAccountMapper implements RowMapper<BankAccount> {

        @Override
        public BankAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            BankAccount bankAccount = new BankAccount(
                    rs.getInt("accountID"),
                    rs.getInt("userID"),
                    rs.getString("accountname"),
                    rs.getFloat("accountbalance")
            );
            return bankAccount;
        }
    }
}
