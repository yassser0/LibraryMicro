package com.booknet.admine_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Arrays;

// Make sure BookDTO is imported or defined in your project
import com.booknet.admine_service.DTO.BookDTO;

@Controller
public class BookDashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/booksDashbord")
    public String showBooks(Model model) {
        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("http://localhost:8083/books", BookDTO[].class);
        List<BookDTO> books = Arrays.asList(response.getBody());
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
}
