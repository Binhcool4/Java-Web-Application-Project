package org.finalproject.controller;

import org.finalproject.entity.User;
import org.finalproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {

        // VALIDATE CONFIRM PASSWORD
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            return "register";
        }

        // VALIDATE ENTITY
        if (result.hasErrors()) {
            return "register";
        }

        String res = userService.register(user);

        if (!res.equals("SUCCESS")) {
            model.addAttribute("error", res);
            return "register";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        // VALIDATION
        StringBuilder errorMsg = new StringBuilder();

        if (email == null || email.trim().isEmpty()) {
            errorMsg.append("Email không được để trống. ");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            errorMsg.append("Email không hợp lệ. ");
        }

        if (password == null || password.trim().isEmpty()) {
            errorMsg.append("Mật khẩu không được để trống. ");
        } else if (password.length() < 6) {
            errorMsg.append("Mật khẩu phải có ít nhất 6 ký tự. ");
        }

        if (errorMsg.length() > 0) {
            model.addAttribute("error", errorMsg.toString().trim());
            return "login";
        }

        User user = userService.login(email.trim(), password);

        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        }

        model.addAttribute("error", "Sai email hoặc mật khẩu");
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
