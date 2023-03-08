package com.jab.bankproject.controller;

import com.jab.bankproject.dao.BankAccountDao;
import com.jab.bankproject.dao.UserDao;
import com.jab.bankproject.entities.BankAccount;
import com.jab.bankproject.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//Controller for the bank accounts page.
@Controller
public class BankAccountController {
    @Autowired
    BankAccountDao bankAccountDao;

    //handle the user loading the accounts page.
    @GetMapping("accounts")
    public String getAccounts(HttpServletRequest request, Model model) {
        //get the current user from the HTTP session.
        User user = (User) request.getSession().getAttribute("bankuser");
        //get all bank accounts owned by the user.
        List<BankAccount> bankAccounts = bankAccountDao.getBankAccountsByUserID(user.getId());

        //add the user and bank accounts to the model, so they can be accessed by Thymeleaf.
        model.addAttribute("bankuser", user);
        model.addAttribute("bankaccounts", bankAccounts);
        return "accounts";
    }

    //handle the user adding a new account.
    @PostMapping("addBankAccount")
    public String addBankAccount(HttpServletRequest request) {
        //get the current user from the HTTP session.
        User user = (User) request.getSession().getAttribute("bankuser");
        //get the details of the new account being added.
        String accountName = request.getParameter("accountName");
        float accountBalance;
        try {
            accountBalance = Float.parseFloat(request.getParameter("accountBalance"));
        } catch (Exception ex) {
            //if the account balance is not a number, use a default instead.
            //it would be better to have an error instead, but this will do for now.
            accountBalance = 500.0f;
        }

        BankAccount bankAccount = new BankAccount(
                -1,
                user.getId(),
                accountName,
                accountBalance
        );

        bankAccount = bankAccountDao.addBankAccount(bankAccount);

        //reload the accounts page.
        return "redirect:/accounts";
    }
}
