package gl2.example.salles.service;

import gl2.example.salles.dto.AuthRequest;
import gl2.example.salles.dto.AuthResponse;
import gl2.example.salles.dto.RegisterRequest;
import gl2.example.salles.model.User;
import gl2.example.salles.model.enums.Role;
import gl2.example.salles.repository.UserRepository;
import gl2.example.salles.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        User user = new User(
                request.getNom(),
                request.getPrenom(),
                request.getEmail(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                Role.User
        );
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
