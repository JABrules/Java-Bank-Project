package com.jab.bankproject.controller;

import com.jab.bankproject.dao.UserDao;
import com.jab.bankproject.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//Controller class for handling users (i.e. the login and signup pages.)
@Controller
public class UserController {
    @Autowired
    UserDao userDao;

    //handle when the user attempts to login
    @GetMapping("login")
    public String login(HttpServletRequest request, Model model) {
        String username = request.getParameter("loginUsername");
        String password = request.getParameter("loginPassword");
        List<User> users = userDao.getUsersByNameAndPassword(username, password);
        //if the user is not found, go back to the login page.
        if (users.size() < 1) return "login";
        User user = users.get(0);

        //add the logged in user to the HTTP session, so that it can be used later.
        request.getSession().setAttribute("bankuser", user);
        request.getSession().setAttribute("bankuserid", user.getId());

        //add the user to the model, so it can be accessed by Thymeleaf.
        model.addAttribute("bankuser", user);
        return "welcome";
    }

    //handle when the user attempts to create an account.
    @GetMapping("signup")
    public String signup(HttpServletRequest request, Model model) {
        String username = request.getParameter("loginUsername");
        String password = request.getParameter("loginPassword");
        User user = new User(-1, username, password);
        user = userDao.addUser(user);

        //add the new user to the HTTP session, so that it can be used later.
        request.getSession().setAttribute("bankuser", user);
        request.getSession().setAttribute("bankuserid", user.getId());

        //add the user to the model so it can be accessed by Thymeleaf.
        model.addAttribute("bankuser", user);
        return "welcome";
    }
}
