package com.studing.cashRegister.controller;

import com.studing.cashRegister.exceptions.MyException;
import com.studing.cashRegister.model.Permission;
import com.studing.cashRegister.model.Report;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.model.UserRole;
import com.studing.cashRegister.service.OrderService;
import com.studing.cashRegister.service.ReportService;
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
 * Servlet Z-report creating
 * @author tHolubets
 */
@WebServlet("/openOrders/zReport")
public class ZReportServlet extends HttpServlet {
    private ReportService reportService = ReportService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ZReportServlet.class);

    /**
     * Method to create and display Z-report
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Report zReport = null;
        try {
            zReport = reportService.makeReport(true);
        } catch (MyException ex) {
            request.setAttribute("errorMessage", ex);
            request.getRequestDispatcher("../error.jsp").forward(request, response);
            return;
        }
        logger.debug("Create xReport reques");

        request.setAttribute("report", zReport);
        request.getRequestDispatcher("report.jsp").forward(request, response);
    }
}
