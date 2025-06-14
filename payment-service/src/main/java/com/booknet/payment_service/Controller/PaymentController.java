package com.booknet.payment_service.Controller;

import com.booknet.payment_service.DTO.BookDTO;
import com.booknet.payment_service.DTO.UserDTO;
import com.booknet.payment_service.Entity.Order;
import com.booknet.payment_service.Service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class PaymentController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    // Affiche la page de paiement
    @GetMapping("/payment")
    public String showPaymentForm(@RequestParam("bookId") Long bookId,
                                   Model model, HttpServletRequest request) {

        BookDTO book = restTemplate.getForObject("http://localhost:8083/books/" + bookId, BookDTO.class);
        model.addAttribute("book", book);
        model.addAttribute("order", new Order());

        return "payment";
    }

    // Enregistre la commande
    @PostMapping("/submitOrder")
    public String submitOrder(@ModelAttribute("order") Order order,
                              @RequestParam("bookId") Long bookId,
                              HttpServletRequest request, Model model) {

        // Récupérer l'utilisateur connecté via session
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
                if (user != null) {
                    order.setEmail(user.getEmail());
                }
            } catch (Exception e) {
                model.addAttribute("error", "Utilisateur non connecté.");
                return "payment";
            }
        }

        // Récupérer infos livre
        BookDTO book = restTemplate.getForObject("http://localhost:8083/books/" + bookId, BookDTO.class);
        order.setBookId(bookId);

        if (book != null) {
            order.setTotalAmount(Double.parseDouble(book.getPrix()));
            order.setBookName(book.getTitle());
        }

        // 🔍 DEBUG des valeurs reçues
        System.out.println("------ Données du formulaire ------");
        System.out.println("Nom: " + order.getName());
        System.out.println("Adresse: " + order.getAddress());
        System.out.println("Ville: " + order.getCity());
        System.out.println("Code postal: " + order.getPostalCode());
        System.out.println("Téléphone: " + order.getPhone());
        System.out.println("Méthode: " + order.getPaymentMethod());
        System.out.println("Email: " + order.getEmail());
        System.out.println("Montant: " + order.getTotalAmount());

        // Sauvegarder dans la base
        orderService.saveOrder(order);

        // Retourner la page de confirmation
        model.addAttribute("order", order);
        return "confirmation";
    }
}
