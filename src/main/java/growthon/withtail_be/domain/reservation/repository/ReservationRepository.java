package growthon.withtail_be.domain.reservation.repository;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.reservation.domain.Reservation;
import growthon.withtail_be.domain.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserId(Long userId);

    boolean existsByReservationNumber(String reservationNumber);

    boolean existsByHospitalAndReservationDateAndReservationTimeAndStatusNot(
            AnimalHospital hospital,
            LocalDate reservationDate,
            LocalTime reservationTime,
            ReservationStatus status
    );

    boolean existsByFuneralAndReservationDateAndReservationTimeAndStatusNot(
            AnimalFuneral funeral,
            LocalDate reservationDate,
            LocalTime reservationTime,
            ReservationStatus status
    );

    List<Reservation> findAllByUser_Id(Long userId);

    boolean existsByHospitalAndReservationDateAndReservationTimeAndStatusNotAndIdNot(
            AnimalHospital hospital,
            LocalDate reservationDate,
            LocalTime reservationTime,
            ReservationStatus status,
            Long id
    );

    boolean existsByFuneralAndReservationDateAndReservationTimeAndStatusNotAndIdNot(
            AnimalFuneral funeral,
            LocalDate reservationDate,
            LocalTime reservationTime,
            ReservationStatus status,
            Long id
    );
}