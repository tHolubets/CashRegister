package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Order;
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
import java.util.List;
import java.util.Optional;

/**
 * Servlet for canceling open orders
 * @author tHolubets
 */
@WebServlet("/openOrders/cancel")
public class OrderCancelServlet extends HttpServlet {
    private OrderService orderService = OrderService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderCancelServlet.class);

    /**
     * Method to cancel open order
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String idStr = request.getParameter("id");
        final long id = Long.valueOf(idStr);
        List<Order> orders = (List)session.getAttribute("openOrders");
        Optional<Order> optionalOrder = orders.stream()
                .filter(order -> order.getId()==id)
                .findAny();
        Order order = optionalOrder.get();
        try {
            orderService.cancelOrder(order);
        } catch (MyException ex) {
            session.setAttribute("errorMessage", ex);
            response.sendRedirect("../openOrders");
        }
        logger.debug("Cancel order({}) request", order.getId());

        session.setAttribute("openOrders", orders);
        response.sendRedirect("../openOrders");
    }
}
