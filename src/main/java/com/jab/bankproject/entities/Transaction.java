package com.jab.bankproject.entities;

import com.jab.bankproject.dao.BankAccountDao;
import com.jab.bankproject.dao.UserDao;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Transaction {
    private int id;
    private int userID;
    private int fromBankAccountID;
    private int toBankAccountID;
    private float transferAmount;
    private Date transferDate;

    //these object variables are intended for displaying information about related objects on the web-page.
    //currently the user variable is not used.
    private User user;
    private BankAccount fromBankAccount;
    private BankAccount toBankAccount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFromBankAccountID() {
        return fromBankAccountID;
    }

    public void setFromBankAccountID(int fromBankAccountID) {
        this.fromBankAccountID = fromBankAccountID;
    }

    public int getToBankAccountID() {
        return toBankAccountID;
    }

    public void setToBankAccountID(int toBankAccountID) {
        this.toBankAccountID = toBankAccountID;
    }

    public float getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(float transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BankAccount getFromBankAccount() {
        return fromBankAccount;
    }

    public void setFromBankAccount(BankAccount fromBankAccount) {
        this.fromBankAccount = fromBankAccount;
    }

    public BankAccount getToBankAccount() {
        return toBankAccount;
    }

    public void setToBankAccount(BankAccount toBankAccount) {
        this.toBankAccount = toBankAccount;
    }

    public Transaction(int id, int userID, int fromBankAccountID, int toBankAccountID, float transferAmount, Date transferDate) {
        this.id = id;
        this.userID = userID;
        this.fromBankAccountID = fromBankAccountID;
        this.toBankAccountID = toBankAccountID;
        this.transferAmount = transferAmount;
        this.transferDate = transferDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getUserID(), other.getUserID())) {
            return false;
        }
        if (!Objects.equals(this.getFromBankAccountID(), other.getFromBankAccountID())) {
            return false;
        }
        if (!Objects.equals(this.getToBankAccountID(), other.getToBankAccountID())) {
            return false;
        }
        if (!Objects.equals(this.getTransferAmount(), other.getTransferAmount())) {
            return false;
        }
        //Some precision is lost in dates when transferred to the database, so ignore comparing them for now.
        /*
        if (!Objects.equals(this.getTransferDate(), other.getTransferDate())) {
            return false;
        }
         */
        return true;
    }

    //sets the object variables for a list of transaction objects.
    public static void setTransactionObjects(List<Transaction> transactions, BankAccountDao bankAccountDao, UserDao userDao) {
        for (Transaction transaction : transactions) {
            transaction.setUser(userDao.getUserByID(transaction.userID));
            transaction.setFromBankAccount(bankAccountDao.getBankAccountByID(transaction.fromBankAccountID));
            transaction.setToBankAccount(bankAccountDao.getBankAccountByID(transaction.toBankAccountID));
        }
    }
}
