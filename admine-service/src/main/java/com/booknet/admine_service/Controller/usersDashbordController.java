package com.booknet.admine_service.Controller;

import com.booknet.admine_service.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class usersDashbordController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/usersDashbord")
    public String usersDashbordPage(Model model, HttpServletRequest request) {
        // Récupère le cookie de session
        String sessionId = Arrays.stream(request.getCookies())
                                 .filter(c -> "JSESSIONID".equals(c.getName()))
                                 .findFirst()
                                 .map(c -> c.getValue())
                                 .orElse(null);

        if (sessionId == null) {
            return "redirect:http://localhost:8081/auth";
        }

        // Appel à user-service pour récupérer l'utilisateur connecté
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> userResponse;
        try {
            userResponse = restTemplate.exchange(
                "http://localhost:8082/currentUser",
                HttpMethod.GET,
                entity,
                UserDTO.class
            );
        } catch (Exception e) {
            return "redirect:http://localhost:8081/auth";
        }

        UserDTO user = userResponse.getBody();
        if (user == null || !"admine@gmail.com".equalsIgnoreCase(user.getEmail())) {
            return "redirect:http://localhost:8081/home";
        }

        // Récupère tous les utilisateurs
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity("http://localhost:8082/users", UserDTO[].class);
        List<UserDTO> users = Arrays.asList(response.getBody());
        model.addAttribute("users", users);

        return "usersDashbord";
    }
}
