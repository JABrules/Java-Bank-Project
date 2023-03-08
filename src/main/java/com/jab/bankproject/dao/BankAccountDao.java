package com.jab.bankproject.dao;

import com.jab.bankproject.entities.BankAccount;

import java.util.List;

public interface BankAccountDao {
    List<BankAccount> getAllBankAccounts();
    List<BankAccount> getBankAccountsByUserID(int userID);
    BankAccount getBankAccountByID(int id);
    BankAccount addBankAccount(BankAccount bankAccount);
    void updateFunds(float newFunds, int id);
    void deleteBankAccountByID(int id);
}
