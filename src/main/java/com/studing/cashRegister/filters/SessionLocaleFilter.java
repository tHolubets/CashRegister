package com.studing.cashRegister.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter for setting language attribute to user session
 *
 * @author tHolubets
 */
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = "/*")
public class SessionLocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        if (req.getParameter("lang") != null) {
            session.setAttribute("lang", req.getParameter("lang"));
        } else {
            if (session.getAttribute("lang") == null)
                session.setAttribute("lang", "en");
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
