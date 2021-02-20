package vn.com.insee.corporate.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.com.insee.corporate.exception.InseeException;
import vn.com.insee.corporate.response.BaseResponse;
import vn.com.insee.corporate.security.InseeUserDetail;
import vn.com.insee.corporate.security.InseeUserDetailService;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.ZaloUserEntity;
import vn.com.insee.corporate.util.HttpUtil;
import vn.com.insee.corporate.util.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Order(2)
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private InseeUserDetailService userUserDetailsService;

    @Autowired
    private ZaloService zaloService;

    public static final String COOKIE_NAME = "_insee_ss";


    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse resp, @NonNull FilterChain chain) throws IOException, ServletException {
        try {
            String _inseeSS = HttpUtil.getCookie(COOKIE_NAME, req);
            if (_inseeSS != null && TokenUtil.isValid(_inseeSS)) {
                Claims claims = TokenUtil.parse(_inseeSS);
                int userId = Integer.parseInt(claims.getAudience());

                UserDetails userDetails = userUserDetailsService.loadUserById(userId);
                if (userDetails != null) {
                    InseeUserDetail inseeUserDetail = (InseeUserDetail) userDetails;
                    if (!inseeUserDetail.getUser().isEnable()) {
                        throw new InseeException();
                    }
                    List<String> lstSession = inseeUserDetail.getUser().getLstSession();
                    if (lstSession == null) {
                        throw new InseeException();
                    }
                    if (lstSession.contains(_inseeSS)) {
                        UsernamePasswordAuthenticationToken auth
                                = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                        SecurityContextHolder.clearContext();
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }

            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            resp.setHeader("Content-Type", "application/json");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setError(HttpStatus.UNAUTHORIZED.value());
            resp.getWriter().println(objectMapper.writeValueAsString(baseResponse));
            return;
        } catch (InseeException ex) {
            ex.printStackTrace();
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            resp.setHeader("Content-Type", "application/json");
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setError(HttpStatus.UNAUTHORIZED.value());
            resp.getWriter().println(objectMapper.writeValueAsString(baseResponse));
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }


}
