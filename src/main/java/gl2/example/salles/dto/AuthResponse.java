package gl2.example.salles.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
