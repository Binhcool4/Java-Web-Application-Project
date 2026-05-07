package org.finalproject.controller;

import org.finalproject.entity.User;
import org.finalproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User formUser,
                                HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        formUser.setId(sessionUser.getId());

        userService.updateProfile(formUser);

        //CẬP NHẬT SESSION SAU KHI CẬP NHẬT DATABASE
        User updatedUser = userService.getUserById(formUser.getId());
        session.setAttribute("user", updatedUser);

        return "redirect:/profile";
    }

}
