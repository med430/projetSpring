package gl2.example.salles.service;

import gl2.example.salles.dto.AuthRequest;
import gl2.example.salles.dto.AuthResponse;
import gl2.example.salles.dto.RegisterRequest;
import gl2.example.salles.model.User;
import gl2.example.salles.model.enums.Role;
import gl2.example.salles.repository.UserRepository;
import gl2.example.salles.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.User)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getIdentifier(), authRequest.getPassword())
        );

        User user = userRepository.findByUsernameOrEmail(authRequest.getIdentifier())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
