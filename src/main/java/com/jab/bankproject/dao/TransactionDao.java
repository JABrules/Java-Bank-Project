package com.jab.bankproject.dao;

import com.jab.bankproject.entities.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getAllTransactions();
    List<Transaction> getAllTransactionsFromAccountID(int accountID);
    List<Transaction> getAllTransactionsToAccountID(int accountID);
    List<Transaction> getTransactionsByUserID(int userID);
    Transaction getTransactionByID(int id);
    Transaction addTransaction(Transaction transaction);
    void deleteTransactionByID(int id);
}
