package game.web.user.controller;

import game.domain.user.model.SignUpRequest;
import game.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequestMapping("/")
public class ControllerUser {
    private final UserService userService;

    public ControllerUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam String login, @RequestParam String password) {
        SignUpRequest signUpRequest = new SignUpRequest(login, password);
        boolean isRegistered = userService.registration(signUpRequest);
        if (isRegistered) {
            return "redirect:/login";
        } else {
            return "register";
        }
    }

    @PostMapping("/login")
    public void login(@RequestParam String login, @RequestParam String password,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hashedPassword = Base64.getEncoder().encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));
        HttpSession session = request.getSession(false);
            session.setAttribute("hashedPassword", hashedPassword);
            session.setAttribute("login", login);
            response.sendRedirect("/");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }
}
