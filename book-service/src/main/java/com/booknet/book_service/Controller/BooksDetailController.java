package com.booknet.book_service.Controller;

import com.booknet.book_service.DTO.BookDTO;
import com.booknet.book_service.DTO.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class BooksDetailController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/BooksDetail")
    public String showBookDetailPage(@RequestParam("id") Long id, Model model, HttpServletRequest request) {

        // Session utilisateur
        String sessionId = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (sessionId != null) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.COOKIE, "JSESSIONID=" + sessionId);
                HttpEntity<Void> entity = new HttpEntity<>(headers);

                ResponseEntity<UserDTO> response = restTemplate.exchange(
                        "http://localhost:8082/currentUser",
                        HttpMethod.GET,
                        entity,
                        UserDTO.class
                );

                model.addAttribute("sessionUser", response.getBody());
            } catch (Exception e) {
                model.addAttribute("sessionUser", null);
            }
        } else {
            model.addAttribute("sessionUser", null);
        }

        // Récupération du livre via son ID
        try {
            ResponseEntity<BookDTO> response = restTemplate.getForEntity(
                    "http://localhost:8083/books/" + id, // Assure-toi que cette route existe dans ton backend
                    BookDTO.class
            );
            model.addAttribute("book", response.getBody());
        } catch (Exception e) {
            model.addAttribute("book", null); // ou rediriger vers une page d’erreur personnalisée
        }

        return "BooksDetail";
    }
}
