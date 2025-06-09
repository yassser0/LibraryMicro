package com.booknet.admine_service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class booksDashbord {
    @GetMapping("/booksDashbord")
    public String booksDashbordpage() {
        return "booksDashbord"; // This should return the name of the view template for the book dashboard
    }
}
