package com.booknet.user_service.Controller;

import com.booknet.user_service.DTO.OrderDTO;
import com.booknet.user_service.Entity.User;
import com.booknet.user_service.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    // ✅ Afficher la page profil avec les livres achetés via OrderDTO
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:http://localhost:8081/auth";
        }

        model.addAttribute("user", user);

        // Récupération des commandes de l'utilisateur depuis payment-service
        String orderUrl = "http://localhost:8085/orders/user?email=" + user.getEmail();
        OrderDTO[] purchasedBooks = new OrderDTO[0];
        try {
            purchasedBooks = restTemplate.getForObject(orderUrl, OrderDTO[].class);
        } catch (Exception e) {
            // Log si nécessaire
        }

        model.addAttribute("purchasedBooks", purchasedBooks);
        return "profile";
    }

    // ✅ Modifier nom
    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam String name,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:http://localhost:8081/auth";

        user.setName(name);
        userService.updateUser(user);
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("infoSuccess", "Informations mises à jour avec succès !");
        return "redirect:http://localhost:8081/profile";
    }

    // ✅ Modifier email
    @PostMapping("/update-email")
    public String updateEmail(@RequestParam String newEmail,
                              @RequestParam String confirmEmail,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:http://localhost:8081/auth";

        if (!newEmail.equals(confirmEmail)) {
            redirectAttributes.addFlashAttribute("emailError", "Les emails ne correspondent pas.");
            return "redirect:http://localhost:8081/profile";
        }

        if (userService.emailExists(newEmail)) {
            redirectAttributes.addFlashAttribute("emailError", "Cet email est déjà utilisé.");
            return "redirect:http://localhost:8081/profile";
        }

        user.setEmail(newEmail);
        userService.updateUser(user);
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("emailSuccess", "Email mis à jour avec succès !");
        return "redirect:http://localhost:8081/profile";
    }

    // ✅ Modifier mot de passe
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:http://localhost:8081/auth";

        if (!userService.checkPassword(user, currentPassword)) {
            redirectAttributes.addAttribute("passwordError", "Mot de passe actuel incorrect.");
            return "redirect:http://localhost:8081/profile?passwordError=Mot+de+passe+actuel+incorrect.";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addAttribute("passwordError", "Les mots de passe ne correspondent pas.");
            return "redirect:http://localhost:8081/profile?passwordError=Les+mots+de+passe+ne+correspondent+pas.";
        }

        userService.changePassword(user, newPassword);
        redirectAttributes.addAttribute("passwordSuccess", "Mot de passe modifié avec succès !");
        return "redirect:http://localhost:8081/profile?passwordSuccess=Mot+de+passe+modifié+avec+succès+!";
    }
}
