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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class BooksAvailableController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/BooksAvailable")
    public String showBooksAvailablePage(Model model, HttpServletRequest request) {
        // Gérer l'utilisateur en session
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

                UserDTO user = response.getBody();
                model.addAttribute("sessionUser", user);
            } catch (Exception e) {
                model.addAttribute("sessionUser", null);
            }
        } else {
            model.addAttribute("sessionUser", null);
        }

        // Récupération des livres depuis book-service
        try {
            ResponseEntity<BookDTO[]> response = restTemplate.getForEntity(
                    "http://localhost:8083/books",
                    BookDTO[].class
            );
            List<BookDTO> books = Arrays.asList(response.getBody());
            model.addAttribute("books", books);
        } catch (Exception e) {
            model.addAttribute("books", List.of()); // en cas d'erreur, on met une liste vide
        }

        return "BooksAvailable";
    }
}
