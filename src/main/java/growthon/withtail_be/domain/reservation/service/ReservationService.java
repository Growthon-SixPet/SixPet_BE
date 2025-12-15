package growthon.withtail_be.domain.reservation.service;

import growthon.withtail_be.domain.reservation.domain.Hospital;
import growthon.withtail_be.domain.reservation.domain.Reservation;
import growthon.withtail_be.domain.reservation.domain.User;
import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.reservation.dto.ReservationResDto;
import growthon.withtail_be.domain.reservation.repository.HospitalRepository;
import growthon.withtail_be.domain.reservation.repository.ReservationRepository;
import growthon.withtail_be.domain.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    public ReservationResDto postReservation(ReservationReqDto reservationReqDto) {
        User user = userRepository.findById(reservationReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Hospital hospital = hospitalRepository.findById(reservationReqDto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        Reservation reservation = new Reservation(
                user,
                reservationReqDto.getPetName(),
                reservationReqDto.getPetAge(),
                reservationReqDto.getPetGender(),
                hospital,
                reservationReqDto.getReservationDate(),
                reservationReqDto.getReservationTime(),
                reservationReqDto.getVisitReason()
        );

        reservationRepository.save(reservation);

        return ReservationResDto.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResDto> getAllReservation(Long userId) {
        return reservationRepository.findAllByUserId(userId)
                .stream()
                .map(ReservationResDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResDto> getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .stream()
                .map(ReservationResDto::from)
                .toList();
    }

    @Transactional
    public ReservationResDto updateReservation(Long reservationId, ReservationReqDto reservationReqDto) {
        User user = userRepository.findById(reservationReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Hospital hospital = hospitalRepository.findById(reservationReqDto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        reservation.update(user, hospital, reservationReqDto);

        return ReservationResDto.from(reservation);
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
