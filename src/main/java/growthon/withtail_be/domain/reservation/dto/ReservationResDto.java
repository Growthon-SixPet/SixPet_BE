package growthon.withtail_be.domain.reservation.dto;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
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

    private Double ratingAvg;
    private Integer reviewCount;
    private Boolean open24h;
    private Boolean nightCare;
    private Boolean openNow;

    private String petName;

    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private String visitReason;

    private String status;

    public static ReservationResDto from(Reservation reservation, boolean openNow) {
        Long targetId = null;
        String targetName = null;

        Double ratingAvg = null;
        Integer reviewCount = null;
        Boolean open24h = null;
        Boolean nightCare = null;

        if (reservation.getTargetType() == TargetType.HOSPITAL) {
            AnimalHospital hospital = reservation.getHospital();
            if (hospital != null) {
                targetId = hospital.getId();
                targetName = hospital.getName();
                ratingAvg = hospital.getRatingAvg();
                reviewCount = hospital.getReviewCount();
                open24h = hospital.isOpen24h();
                nightCare = hospital.isNightCare();
            }
        } else if (reservation.getTargetType() == TargetType.FUNERAL) {
            AnimalFuneral funeral = reservation.getFuneral();
            if (funeral != null) {
                targetId = funeral.getId();
                targetName = funeral.getName();
                ratingAvg = funeral.getRatingAvg();
                reviewCount = funeral.getReviewCount();
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
                .ratingAvg(ratingAvg)
                .reviewCount(reviewCount)
                .open24h(open24h)
                .nightCare(nightCare)
                .openNow(openNow)
                .petName(reservation.getPetName())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .visitReason(reservation.getVisitReason())
                .status(reservation.getStatus().name())
                .build();
    }
}
