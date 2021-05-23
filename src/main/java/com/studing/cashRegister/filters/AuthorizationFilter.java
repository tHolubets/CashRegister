package com.studing.cashRegister.filters;

import com.studing.cashRegister.model.Permission;
import com.studing.cashRegister.model.User;
import com.studing.cashRegister.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filter for checking if the user has permission to perform a certain action
 * @author tHolubets
 */
@WebFilter("/*")
public class AuthorizationFilter implements Filter {
    private static String baseUrl = "http://localhost:8080/cashRegister_war_exploded/";
    private static Map<String, Permission> neededPermissions;
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    static {
        neededPermissions = new HashMap<>();
        neededPermissions.put("goods", Permission.ADD_GOODS);
        neededPermissions.put("order/addGoods", Permission.UPDATE_ORDER);
        neededPermissions.put("goods/edit", Permission.ADD_GOODS);
        neededPermissions.put("openOrders", Permission.CANCEL_ORDER);
        neededPermissions.put("openOrders/cancel", Permission.CANCEL_ORDER);
        neededPermissions.put("order/close", Permission.CLOSE_ORDER);
        neededPermissions.put("openOrders/edit", Permission.CANCEL_ORDER);
        neededPermissions.put("order", Permission.CREATE_ORDER);
        neededPermissions.put("openOrders/xReport", Permission.CREATE_REPORT);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            url = url.substring(baseUrl.length());
            User user = (User) ((HttpServletRequest) request).getSession().getAttribute("user");
            Permission neededPermission = neededPermissions.get(url);
            if (neededPermission != null && (user == null || !user.getRole().hasPermission(neededPermission))) {
                logger.info("Attempt to access the page without the appropriate rights");

                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        }
        chain.doFilter(request, response);
    }
}
