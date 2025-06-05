package com.booknet.admine_service.Controller;

import com.booknet.admine_service.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class usersDashbordController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/usersDashbord")
    public String usersDashbordPage(Model model) {
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity("http://localhost:8082/users", UserDTO[].class);
        List<UserDTO> users = Arrays.asList(response.getBody());
        model.addAttribute("users", users);
        return "usersDashbord";
    }
}
