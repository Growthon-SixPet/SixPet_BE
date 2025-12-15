package growthon.withtail_be.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationReqDto {

    private Long userId;

    private Long hospitalId;

    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private String petName;
    private Integer petAge;
    private String petGender;

    private String visitReason;
}
