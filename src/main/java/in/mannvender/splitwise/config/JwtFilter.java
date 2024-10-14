package in.mannvender.splitwise.config;

import in.mannvender.splitwise.config.UserContext;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private static final List<String> EXCLUDED_URLS = Arrays.asList("/auth/login", "/auth/signup", "/auth/validate-token");

    @Autowired
    public JwtFilter(@Lazy JwtUtil jwtUtil, IUserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String requestURI = request.getRequestURI();
    if (EXCLUDED_URLS.contains(requestURI)) {
        filterChain.doFilter(request, response);
        return;
    }

    // Retrieve cookies from the request
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        // Find the token cookie
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (tokenCookie != null) {
            String token = tokenCookie.getValue();
            try {
                // Extract user ID and expiration time from the token
                Long userId = jwtUtil.extractUserId(token);
                System.out.println("User ID: " + userId);
                Long exp = jwtUtil.extractExp(token);
                System.out.println("Token Expiration Time: " + exp);
                // Check if the token is expired
                if (exp < System.currentTimeMillis() / 1000) {
                    System.out.println("Token expired");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                // Retrieve the user by ID
                User user = userService.getUserById(userId).orElse(null);
                System.out.println("User: " + user);
                if (user != null) {
                    // Set the user in the UserContext
                    UserContext.setUser(user);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Invalid token");
                System.out.println(e.getMessage());
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

    // Proceed with the filter chain
    filterChain.doFilter(request, response);
    // Clear the UserContext after the request is processed
    UserContext.clear();
}
}