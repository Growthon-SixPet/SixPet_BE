package growthon.withtail_be.domain.reservation.dto;

import growthon.withtail_be.domain.reservation.domain.Hospital;
import growthon.withtail_be.domain.reservation.domain.Reservation;
import growthon.withtail_be.domain.reservation.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class ReservationResDto {

    private Long reservationId;

    private String userName;
    private String phoneNumber;

    private String petName;
    private Integer petAge;
    private String petGender;

    private String hospitalName;

    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private String visitReason;

    public static ReservationResDto from(Reservation reservation) {
        return ReservationResDto.builder()
                .reservationId(reservation.getId())
                .userName(reservation.getUser().getName())
                .phoneNumber(reservation.getUser().getPhoneNumber())
                .petName(reservation.getPetName())
                .petAge(reservation.getPetAge())
                .petGender(reservation.getPetGender())
                .hospitalName(reservation.getHospital().getName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .visitReason(reservation.getVisitReason())
                .build();
    }
}
