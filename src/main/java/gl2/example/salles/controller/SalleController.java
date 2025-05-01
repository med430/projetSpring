package gl2.example.salles.controller;

import gl2.example.salles.dto.SalleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/salles")
public class SalleController {
    @PostMapping
    public ResponseEntity<SalleResponse> createSalle() {

    }
}
