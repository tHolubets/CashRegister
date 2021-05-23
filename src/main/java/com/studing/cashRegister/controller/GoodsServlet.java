package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Goods;
import com.studing.cashRegister.model.Permission;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.model.UserRole;
import com.studing.cashRegister.service.GoodsService;
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

/**
 * Servlet for goods display
 * @author tHolubets
 */
@WebServlet("/goods")
public class GoodsServlet extends HttpServlet {
    private GoodsService goodsService = GoodsService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(GoodsServlet.class);

    /**
     * Method to show list of goods
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("Goods get request");

        List<Goods> goods = null;
        try {
            goods = goodsService.getAll();
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        request.setAttribute("goods", goods);
        request.getRequestDispatcher("goods.jsp").forward(request, response);
    }
}
