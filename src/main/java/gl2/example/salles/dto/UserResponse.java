package gl2.example.salles.dto;

import gl2.example.salles.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.nom = user.getNom();
        this.prenom = user.getPrenom();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
