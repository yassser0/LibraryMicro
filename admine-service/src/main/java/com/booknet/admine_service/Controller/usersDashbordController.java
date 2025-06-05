package com.booknet.admine_service.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller

public class usersDashbordController {
      @GetMapping("/usersDashbord")
    public String usersDashbordPage() {
        return "usersDashbord"; 
    }
}
    

