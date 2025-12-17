package growthon.withtail_be.domain.reservation.dto;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.reservation.domain.Reservation;
import growthon.withtail_be.domain.reservation.domain.TargetType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
public class ReservationResDto {

    private Long reservationId;
    private String reservationNumber;

    private String ownerName;
    private String phoneNumber;

    private TargetType targetType;
    private Long targetId;
    private String targetName;

    private String petName;

    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private String visitReason;

    private String status; // 필요하면 ReservationStatus로 바꿔도 됨

    public static ReservationResDto from(Reservation reservation) {
        Long targetId = null;
        String targetName = null;

        if (reservation.getTargetType() == TargetType.HOSPITAL) {
            Hospital hospital = reservation.getHospital();
            if (hospital != null) {
                targetId = hospital.getId();
                targetName = hospital.getName();
            }
        } else if (reservation.getTargetType() == TargetType.FUNERAL) {
            Funeral funeral = reservation.getFuneral();
            if (funeral != null) {
                targetId = funeral.getId();
                targetName = funeral.getName();
            }
        }

        return ReservationResDto.builder()
                .reservationId(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .ownerName(reservation.getOwnerName())
                .phoneNumber(reservation.getPhoneNumber())
                .targetType(reservation.getTargetType())
                .targetId(targetId)
                .targetName(targetName)
                .petName(reservation.getPetName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .visitReason(reservation.getVisitReason())
                .status(reservation.getStatus().name())
                .build();
    }
}
