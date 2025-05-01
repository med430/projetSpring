package gl2.example.salles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String password;
}
