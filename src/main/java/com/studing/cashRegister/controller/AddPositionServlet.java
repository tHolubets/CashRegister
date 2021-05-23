package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.*;
import com.studing.cashRegister.service.GoodsService;
import com.studing.cashRegister.service.OrderService;
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
 * Servlet for goods addition to orders
 * @author tHolubets
 */
@WebServlet("/order/addGoods")
public class AddPositionServlet extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(AddPositionServlet.class);

    /**
     * Method to add new position of goods to check(order)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("addPositionError");
        String name = request.getParameter("gname");
        String idStr = request.getParameter("gid");
        long id = 0;
        if(!idStr.isEmpty()){
            id = Long.valueOf(request.getParameter("gid"));
        }
        int quantity = Integer.parseInt(request.getParameter("gquantity"));
        OrderedGoods orderedGoods = null;
        Order order = ((Order)session.getAttribute("order"));
        try {
            orderedGoods = orderService.addGoods(id, name, quantity, order);
        } catch (MyException ex) {
            session.setAttribute("errorMessage", ex);
            response.sendRedirect("error.jsp");
        }
        if(orderedGoods == null){
            session.setAttribute("addPositionError", "No such product was found");
            response.sendRedirect("../order");
            return;
        }
        if(orderedGoods.getQuantity()!=quantity){
            if(orderedGoods.getQuantity()==0){
                session.setAttribute("addPositionError", "The product is not available");
            }
            else {
                session.setAttribute("addPositionError", "Insufficient quantity of goods");
            }
        }
        logger.debug("Position({}) added to the order({})", orderedGoods.getId(), order.getId());
        session.setAttribute("order", order);
        response.sendRedirect("../order");
    }
}
