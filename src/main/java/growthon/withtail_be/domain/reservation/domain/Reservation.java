package growthon.withtail_be.domain.reservation.domain;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private AnimalHospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id")
    private AnimalFuneral funeral;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    private String visitReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    public Reservation(
            String reservationNumber,
            User user,
            TargetType targetType,
            AnimalHospital hospital,
            AnimalFuneral funeral,
            String ownerName,
            String phoneNumber,
            String petName,
            LocalDate reservationDate,
            LocalTime reservationTime,
            String visitReason
    ) {
        this.reservationNumber = reservationNumber;
        this.user = user;
        this.targetType = targetType;
        this.hospital = hospital;
        this.funeral = funeral;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.petName = petName;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.visitReason = visitReason;
        this.status = ReservationStatus.BEFORE_VISIT;
    }

    public void update(
            TargetType targetType,
            AnimalHospital hospital,
            AnimalFuneral funeral,
            ReservationReqDto dto
    ) {
        this.targetType = targetType;
        this.hospital = hospital;
        this.funeral = funeral;
        this.ownerName = dto.getOwnerName();
        this.phoneNumber = dto.getPhoneNumber();
        this.petName = dto.getPetName();
        this.reservationDate = dto.getReservationDate();
        this.reservationTime = dto.getReservationTime();
        this.visitReason = dto.getVisitReason();
    }

    public void changeStatus(ReservationStatus status) {
        this.status = status;
    }
}
