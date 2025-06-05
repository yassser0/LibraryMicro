package com.booknet.user_service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class BooksAvailableController {
    @GetMapping("/BooksAvailable")
    public String BooksAvailablePage() {
        return "booksAvailable"; 
    }
}
