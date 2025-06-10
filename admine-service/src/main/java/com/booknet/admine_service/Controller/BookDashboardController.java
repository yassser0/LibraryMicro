package com.booknet.admine_service.Controller;

import com.booknet.admine_service.DTO.BookDTO;
import com.booknet.admine_service.DTO.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class BookDashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/booksDashbord")
    public String showBooks(Model model, HttpServletRequest request) {
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

        // 3. Récupérer les livres
        List<BookDTO> books = List.of(); // valeur par défaut pour éviter le null
        try {
            ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("http://localhost:8083/books", BookDTO[].class);
            if (response.getBody() != null) {
                books = Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            // Tu peux ajouter un log ici si tu veux traquer les erreurs de récupération
        }

        model.addAttribute("books", books);
        model.addAttribute("newBook", new BookDTO());

        return "booksDashbord";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute BookDTO newBook) {
        restTemplate.postForEntity("http://localhost:8083/books", newBook, BookDTO.class);
        return "redirect:http://localhost:8081/booksDashbord";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id) {
        restTemplate.delete("http://localhost:8083/books/" + id);
        return "redirect:http://localhost:8081/booksDashbord";
    }
    @GetMapping("/editBook/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        BookDTO book = restTemplate.getForObject("http://localhost:8083/books/" + id, BookDTO.class);
        model.addAttribute("book", book);
        return "editBook";
    }
    @PostMapping("/updateBook")
    public String updateBook(@ModelAttribute BookDTO book) {
        restTemplate.put("http://localhost:8083/books/" + book.getId(), book);
        return "redirect:http://localhost:8081/booksDashbord";
    }
}
