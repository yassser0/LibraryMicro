package com.booknet.user_service.Controller;

import com.booknet.user_service.DTO.UserDTO;
import com.booknet.user_service.Entity.User;
import com.booknet.user_service.Service.UserService;
import jakarta.servlet.http.HttpSession;
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
    public String authPage(Model model,
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
    public String handleLoginGet() {
        return "redirect:http://localhost:8081/auth";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
        try {
            if (userService.emailExists(user.getEmail())) {
                redirectAttributes.addAttribute("error", "Cet email est déjà utilisé.");
                return "redirect:http://localhost:8081/auth";
            }

            userService.registerUser(user);
            session.setAttribute("user", user); 

            return "redirect:http://localhost:8081/home";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
            return "redirect:http://localhost:8081/auth";
        }
    }

  @PostMapping("/login")
public String loginUser(@RequestParam String email,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {
    
    return userService.login(email, password)
            .map(user -> {
                // Stocker l'utilisateur dans la session
                session.setAttribute("user", user);

                // Si l'admin se connecte, rediriger vers le tableau de bord admin
                if ("admine@gmail.com".equalsIgnoreCase(user.getEmail())) {
                    return "redirect:http://localhost:8081/usersDashbord";
                }

                // Sinon, rediriger l'utilisateur normal vers sa page d'accueil
                return "redirect:http://localhost:8081/home";
            })
            .orElseGet(() -> {
                // En cas d’échec de connexion, renvoyer un message d’erreur
                redirectAttributes.addAttribute("error", "Email ou mot de passe incorrect.");
                return "redirect:http://localhost:8081/auth";
            });
}

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); 
        redirectAttributes.addAttribute("success", "Déconnexion réussie.");
          return "redirect:http://localhost:8081/home";
    }
    @GetMapping("/currentUser")
@ResponseBody
public UserDTO getCurrentUser(HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) return null;

    return new UserDTO(user.getId(), user.getName(), user.getEmail());
}
}
