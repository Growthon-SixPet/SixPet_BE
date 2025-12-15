package growthon.withtail_be.domain.reservation.controller;

import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.reservation.dto.ReservationResDto;
import growthon.withtail_be.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResDto> postReservation(@RequestBody ReservationReqDto reservationReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.postReservation(reservationReqDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResDto>> getAllReservation(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getAllReservation(userId));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<List<ReservationResDto>> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservation(reservationId));
    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationResDto> updateReservation(@PathVariable Long reservationId, @RequestBody ReservationReqDto reservationReqDto) {
        ReservationResDto updatedReservation = reservationService.updateReservation(reservationId, reservationReqDto);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationResDto> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
