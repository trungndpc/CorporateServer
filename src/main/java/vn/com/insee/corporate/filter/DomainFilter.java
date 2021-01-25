package vn.com.insee.corporate.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.com.insee.corporate.common.Constant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(3)
public class DomainFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isNeed2Filter(request)) {
            System.out.println("DomainFilter");
            String domain =  request.getServerName();
            System.out.println(domain);
            System.out.println(Constant.ADMIN_DOMAIN);
            if (Constant.ADMIN_DOMAIN.equals(domain)) {
                System.out.println("DomainFilter ADMIN_DOMAIN");
                request.getRequestDispatcher("/" + Constant.PREFIX_ADMIN_CONTROLLER + request.getRequestURI()).forward(request, response);
                return;
            }

            if (Constant.CLIENT_DOMAIN.equals(domain)) {
                System.out.println("DomainFilter CLIENT_DOMAIN");
                request.getRequestDispatcher("/" + Constant.PREFIX_CLIENT_CONTROLLER + request.getRequestURI()).forward(request, response);
                return;
            }
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isNeed2Filter(HttpServletRequest request){
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api")) {
            return false;
        }
        if (requestURI.startsWith("/authen")) {
            return false;
        }
        return true;
    }
}
