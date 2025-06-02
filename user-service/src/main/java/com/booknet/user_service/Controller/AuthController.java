package com.booknet.user_service.Controller;

import com.booknet.user_service.Entity.User;
import com.booknet.user_service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth")
    public String AuthPage(Model model,
                           @RequestParam(required = false) String success,
                           @RequestParam(required = false) String error) {
        if (success != null) {
            model.addAttribute("message", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "auth";
    }

    @GetMapping("/login")
    public String handleLoginGet(RedirectAttributes redirectAttributes) {
        return "redirect:/auth";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (userService.emailExists(user.getEmail())) {
                redirectAttributes.addAttribute("error", "Cet email est déjà utilisé.");
                return "redirect:/auth";
            }

            userService.registerUser(user);
            redirectAttributes.addAttribute("success", "Inscription réussie !");
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
        }

        return "redirect:/auth";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {
        if (userService.login(email, password).isPresent()) {
            redirectAttributes.addAttribute("success", "Bienvenue !");
            return "redirect:/auth";
        } else {
            redirectAttributes.addAttribute("error", "Email ou mot de passe incorrect.");
            return "redirect:/auth";
        }
    }
}
