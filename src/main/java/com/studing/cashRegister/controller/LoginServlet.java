package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for login
 * @author tHolubets
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    /**
     * Method to check login data and redirect to the other pages
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("uname");
        String password = request.getParameter("upassword");

        User user = new User(login, password);
        boolean isLoginSuccessful = false;
        try {
            isLoginSuccessful = userService.checkUserLogin(user);
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        if (isLoginSuccessful) {
            logger.info("Successful login = {}", login);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            switch (user.getRole()) {
                case COMMODITY_EXPERT:
                    response.sendRedirect("goods");
                    break;
                case CASHIER:
                    response.sendRedirect("order");
                    break;
                case SENIOR_CASHIER:
                    response.sendRedirect("openOrders");
                    break;
            }
        } else {
            logger.info("Unsuccessful login = {}", login);
            request.setAttribute("errMessage", "Incorrect login data!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
