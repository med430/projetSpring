package gl2.example.salles.service;

import gl2.example.salles.dto.UserRequest;
import gl2.example.salles.model.User;
import gl2.example.salles.model.enums.Role;
import gl2.example.salles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserRequest userRequest) {
        User user = User.builder()
                .nom(userRequest.getNom())
                .prenom(userRequest.getPrenom())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.User)
                .build();
        userRepository.save(user);
        return user;
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow();
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow();
    }

    public List<User> findByNom(String nom) {
        return userRepository.findAll()
                .stream().filter(user -> user.getNom().equals(nom)).toList();
    }

    public List<User> findByPrenom(String prenom) {
        return userRepository.findAll()
                .stream().filter(user -> user.getPrenom().equals(prenom)).toList();
    }

    public List<User> findByRole(String role) {
        return userRepository.findAll()
                .stream().filter(user -> user.getRole().toString().equals(role)).toList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow();
        user.setNom(userRequest.getNom());
        user.setPrenom(userRequest.getPrenom());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
