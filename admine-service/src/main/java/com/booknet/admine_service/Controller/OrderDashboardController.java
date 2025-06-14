package com.booknet.admine_service.Controller;

import com.booknet.admine_service.DTO.OrderDTO;
import com.booknet.admine_service.DTO.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class OrderDashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ordersDashbord")
    public String showOrders(Model model, HttpServletRequest request) {
        // 1. Extraire la session
        String sessionId = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (sessionId == null) {
            return "redirect:http://localhost:8081/auth";
        }

        // 2. Authentifier l'utilisateur
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

        // 3. Récupérer les commandes
        OrderDTO[] orders = restTemplate.getForObject("http://localhost:8085/orders", OrderDTO[].class);

        // 4. Ajouter au modèle
        model.addAttribute("orders", orders);
        int totalSold = orders != null ? orders.length : 0;
        model.addAttribute("totalSold", totalSold);

        return "ordersDashbord";
    }
}
