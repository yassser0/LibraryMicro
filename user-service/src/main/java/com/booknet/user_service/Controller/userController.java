package com.booknet.user_service.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.booknet.user_service.DTO.UserDTO;
import com.booknet.user_service.Service.UserService;

@RestController
@RequestMapping("/users")
public class userController {

    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUserDTOs(); // Cette méthode récupère depuis la base de données
        return ResponseEntity.ok(users);
    }
}
