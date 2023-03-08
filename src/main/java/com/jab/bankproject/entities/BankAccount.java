package com.jab.bankproject.entities;

import java.util.Objects;

public class BankAccount {
    private int id;
    private int userID;
    private String accountName;
    private float accountBalance;

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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BankAccount(int id, int userID, String accountName, float accountBalance) {
        this.id = id;
        this.userID = userID;
        this.accountName = accountName;
        this.accountBalance = accountBalance;
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
        final BankAccount other = (BankAccount) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getUserID(), other.getUserID())) {
            return false;
        }
        if (!Objects.equals(this.getAccountName(), other.getAccountName())) {
            return false;
        }
        if (!Objects.equals(this.getAccountBalance(), other.getAccountBalance())) {
            return false;
        }
        return true;
    }
}
