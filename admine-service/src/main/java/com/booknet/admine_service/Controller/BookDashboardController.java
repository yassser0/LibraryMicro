package com.booknet.admine_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Arrays;
import com.booknet.admine_service.DTO.BookDTO;

@Controller
public class BookDashboardController {

    @Autowired private RestTemplate restTemplate;

    @GetMapping("/BooksDashboard")
    public String showBooks(Model model) {
        ResponseEntity<BookDTO[]> response = restTemplate.getForEntity("http://localhost:8083/books", BookDTO[].class);
        List<BookDTO> books = Arrays.asList(response.getBody());
        model.addAttribute("books", books);
        model.addAttribute("bookForm", new BookDTO());
        return "BooksDashboard";
    }

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute("bookForm") BookDTO bookDTO) {
        restTemplate.postForEntity("http://localhost:8083/books", bookDTO, BookDTO.class);
        return "redirect:/BooksDashboard";
    }
}
