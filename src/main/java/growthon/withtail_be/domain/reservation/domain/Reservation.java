package growthon.withtail_be.domain.reservation.domain;

import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private int petAge;

    @Column(nullable = false)
    private String petGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Column
    private String visitReason;

    public Reservation(User user, String petName, int petAge, String petGender, Hospital hospital, LocalDate reservationDate, LocalTime reservationTime, String visitReason) {
        this.user = user;
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;
        this.hospital = hospital;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.visitReason = visitReason;
    }

    public void update(User user, Hospital hospital, ReservationReqDto dto) {
        this.user = user;
        this.hospital = hospital;
        this.petName = dto.getPetName();
        this.petAge = dto.getPetAge();
        this.petGender = dto.getPetGender();
        this.reservationDate = dto.getReservationDate();
        this.reservationTime = dto.getReservationTime();
        this.visitReason = dto.getVisitReason();
    }
}
