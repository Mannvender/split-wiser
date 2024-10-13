package in.mannvender.splitwise.config;

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
                    // vlidate exp with current epoch time
                    if(exp < System.currentTimeMillis()){
                        System.out.println("Token expired");
                        throw new RuntimeException("Token expired");
                    }
                    User user = userService.getUserById(userId).orElse(null);
                    System.out.println("User found in token: " + user);
                    if (user != null) {
                        System.out.println("User found in token: " + user);
                        UserContext.setUser(user);
                    }
                } catch (Exception e) {
                    // Handle token parsing or user fetching errors
                }
            }
        }
        filterChain.doFilter(request, response);
        UserContext.clear();
    }
}
