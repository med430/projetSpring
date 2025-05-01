package gl2.example.salles.dto;

import lombok.Data;

@Data
public class SalleRequest {
    private Long id;
    private String nom;
    private String capacite;
}
