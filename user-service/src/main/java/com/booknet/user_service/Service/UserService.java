package com.booknet.user_service.Service;

import com.booknet.user_service.DTO.UserDTO;
import com.booknet.user_service.Entity.User;
import com.booknet.user_service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inscription
    public void registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    // Connexion
    public Optional<User> login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // Vérifier si l'email existe déjà
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Mettre à jour un utilisateur (profil ou email)
    public void updateUser(User user) {
        userRepository.save(user);
    }

    // Vérifier que le mot de passe fourni correspond à l'utilisateur (avant changement de mot de passe)
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Changer le mot de passe (en hashant)
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public List<UserDTO> getAllUserDTOs() {
    return userRepository.findAll().stream()
            .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
            .collect(Collectors.toList());
}

}
