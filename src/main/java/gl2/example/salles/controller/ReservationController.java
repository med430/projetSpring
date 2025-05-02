package gl2.example.salles.controller;

import gl2.example.salles.dto.ReservationRequest;
import gl2.example.salles.dto.ReservationResponse;
import gl2.example.salles.model.Reservation;
import gl2.example.salles.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.createReservation(reservationRequest);
        ReservationResponse reservationResponse = new ReservationResponse(reservation);
        return ResponseEntity.ok(reservationResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAllReservation() {
        List<Reservation> reservations = reservationService.findAll();
        List<ReservationResponse> reservationResponses = reservations.stream().map(ReservationResponse::new).toList();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> findReservationById(@PathVariable Long id) {
        ReservationResponse reservationResponse = new ReservationResponse(reservationService.findByID(id));
        return ResponseEntity.ok(reservationResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.findByID(id);
        reservationService.updateReservation(id, reservationRequest);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
