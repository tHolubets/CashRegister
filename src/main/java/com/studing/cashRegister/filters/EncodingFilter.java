package com.studing.cashRegister.filters;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;
/**
 * Filter for correct work with the Cyrillic alphabet
 * @author tHolubets
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(request.getCharacterEncoding() == null){
            request.setCharacterEncoding("UTF-8");
        }

        chain.doFilter(request, response);
    }
}
