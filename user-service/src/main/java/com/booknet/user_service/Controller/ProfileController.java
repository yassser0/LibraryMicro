package com.booknet.user_service.Controller;

import com.booknet.user_service.Entity.User;
import com.booknet.user_service.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // Afficher la page profil
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:http://localhost:8081/auth";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    // Update profil (nom, etc)
    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam String name,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:http://localhost:8081/auth";

        user.setName(name); // Change selon le nom de ton champ
        userService.updateUser(user);
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("infoSuccess", "Informations mises à jour avec succès !");
        return "redirect:http://localhost:8081/profile";
    }

    // Update email
    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String newEmail,
                              @RequestParam String confirmEmail,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:http://localhost:8081/auth";

        if (!newEmail.equals(confirmEmail)) {
            redirectAttributes.addFlashAttribute("emailSuccess", null);
            redirectAttributes.addFlashAttribute("emailError", "Les emails ne correspondent pas.");
            return "redirect:http://localhost:8081:/profile";
        }

        if (userService.emailExists(newEmail)) {
            redirectAttributes.addFlashAttribute("emailSuccess", null);
            redirectAttributes.addFlashAttribute("emailError", "Cet email est déjà utilisé.");
            return "redirect:http://localhost:8081/profile";
        }

        user.setEmail(newEmail);
        userService.updateUser(user);
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("emailSuccess", "Email mis à jour avec succès !");
        return "redirect:http://localhost:8081/profile";
    }

    // Update password
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:http://localhost:8081/auth";

        if (!userService.checkPassword(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "Mot de passe actuel incorrect.");
            return "redirect:http://localhost:8081/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "Les mots de passe ne correspondent pas.");
            return "redirect:http://localhost:8081/profile";
        }

        userService.changePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("passwordSuccess", "Mot de passe modifié avec succès !");
        return "redirect:http://localhost:8081/profile";
    }
}
