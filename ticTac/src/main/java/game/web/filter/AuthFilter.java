package game.web.filter;

import game.datasource.user.model.UserData;
import game.domain.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthFilter extends GenericFilterBean {
    private final UserService userService;

    public AuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(true);
        String path = httpRequest.getRequestURI();

        if (path.equals("/login") || path.equals("/register") || path.equals("/favicon.ico")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String hashedPassword = (String) session.getAttribute("hashedPassword");
        if (hashedPassword == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid authentication");
            return;
        } else {
            UUID user = userService.authenticate(hashedPassword);
            if (user != null) {
                Optional<UserData> isAuthenticated = userService.authorize(user.toString());
                if (isAuthenticated.isPresent()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid authentication");
                    return;
                }
            } else {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid authentication");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}


