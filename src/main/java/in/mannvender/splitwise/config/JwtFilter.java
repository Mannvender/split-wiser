package in.mannvender.splitwise.config;

import in.mannvender.splitwise.annotations.OpenEndpoint;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.services.interfaces.IUserService;
import in.mannvender.splitwise.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final IUserService userService;

    @Autowired
    public JwtFilter(@Lazy JwtUtil jwtUtil, IUserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> attributes = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (attributes != null) {
            Object handler = attributes.get(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (handlerMethod.hasMethodAnnotation(OpenEndpoint.class)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie tokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (tokenCookie != null) {
                String token = tokenCookie.getValue();
                try {
                    Long userId = jwtUtil.extractUserId(token);
                    Long exp = jwtUtil.extractExp(token);
                    if (exp < System.currentTimeMillis()) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    User user = userService.getUserById(userId).orElse(null);
                    if (user != null) {
                        UserContext.setUser(user);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
        UserContext.clear();
    }
}