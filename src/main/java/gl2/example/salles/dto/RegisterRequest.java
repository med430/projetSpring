package gl2.example.salles.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String password;
}
