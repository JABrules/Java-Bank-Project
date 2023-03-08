package com.jab.bankproject.controller;

import com.jab.bankproject.dao.BankAccountDao;
import com.jab.bankproject.dao.TransactionDao;
import com.jab.bankproject.dao.UserDao;
import com.jab.bankproject.entities.BankAccount;
import com.jab.bankproject.entities.Transaction;
import com.jab.bankproject.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;

//Controller for the transactions page.
@Controller
public class TransactionController {
    @Autowired
    UserDao userDao;
    @Autowired
    BankAccountDao bankAccountDao;
    @Autowired
    TransactionDao transactionDao;

    //handle the user loading the transactions page.
    @GetMapping("transactions")
    public String getTransactions(HttpServletRequest request, Model model) {
        //get the current user from the HTTP session.
        User user = (User) request.getSession().getAttribute("bankuser");
        //get all transactions made by the user.
        List<Transaction> transactions = transactionDao.getTransactionsByUserID(user.getId());
        //set the user and bank account object variables for the transaction objects.
        Transaction.setTransactionObjects(transactions, bankAccountDao, userDao);
        //get all bank accounts belonging to the user.
        List<BankAccount> bankAccounts = bankAccountDao.getBankAccountsByUserID(user.getId());

        //add the user, bank accounts and transactions to the model, so they can be accessed by Thymeleaf.
        model.addAttribute("bankuser", user);
        model.addAttribute("bankaccounts", bankAccounts);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }

    //handle the user performing a transaction.
    @PostMapping("addTransaction")
    public String addTransaction(HttpServletRequest request) {
        //get the current user from the HTTP session.
        User user = (User) request.getSession().getAttribute("bankuser");
        //get the details for the new transaction.
        int fromAccountID = Integer.parseInt(request.getParameter("fromAccountID"));
        int toAccountID = Integer.parseInt(request.getParameter("toAccountID"));
        float transferAmount;
        try {
            transferAmount = Float.parseFloat(request.getParameter("transferAmount"));
        } catch (Exception ex) {
            transferAmount = 1.0f;
        }
        //creating a new date object defaults to the current time.
        Date transferDate = new Date();

        Transaction transaction = new Transaction(
                -1,
                user.getId(),
                fromAccountID,
                toAccountID,
                transferAmount,
                transferDate
        );

        transactionDao.addTransaction(transaction);

        //we also need to actually tranfer the funds between the two accounts.
        BankAccount fromBankAccount = bankAccountDao.getBankAccountByID(fromAccountID);
        bankAccountDao.updateFunds(fromBankAccount.getAccountBalance() - transferAmount, fromAccountID);

        BankAccount toBankAccount = bankAccountDao.getBankAccountByID(toAccountID);
        bankAccountDao.updateFunds(toBankAccount.getAccountBalance() + transferAmount, toAccountID);

        //reload the transactions page.
        return "redirect:/transactions";
    }
}
