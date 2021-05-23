package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.model.Permission;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.model.UserRole;
import com.studing.cashRegister.service.GoodsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for goods edition and addition
 * @author tHolubets
 */
@WebServlet("/goods/edit")
public class GoodsEditServlet extends HttpServlet {
    private GoodsService goodsService = GoodsService.getInstance();

    /**
     * Method to get goods view for editing
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.valueOf(request.getParameter("id"));
        Goods goods = null;
        try {
            goods = goodsService.getById(id);
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        request.setAttribute("goods", goods);
        request.getRequestDispatcher("goodsEdit.jsp").forward(request, response);
    }

    /**
     * Method to edit and add goods
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        long id = 0;
        if(idStr!=null){
            id = Long.valueOf(idStr);
        }
        String name = request.getParameter("gname");
        String description = request.getParameter("gdescription");
        double price = Double.valueOf(request.getParameter("gprice"));
        int quantity = Integer.valueOf(request.getParameter("gquantity"));
        Goods goods = new Goods(id, name, description, price, quantity);
        try {
            goodsService.save(goods);
        } catch (MyException ex) {
            request.getSession().setAttribute("errorMessage", ex);
            response.sendRedirect("../error.jsp");
            return;
        }
        response.sendRedirect("../goods");
    }
}
