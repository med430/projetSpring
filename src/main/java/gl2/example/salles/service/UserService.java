package gl2.example.salles.service;

import gl2.example.salles.dto.UserRequest;
import gl2.example.salles.model.User;
import gl2.example.salles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        User user = User.builder()
                .nom(userRequest.getNom())
                .prenom(userRequest.getPrenom())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        userRepository.save(user);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }
}
