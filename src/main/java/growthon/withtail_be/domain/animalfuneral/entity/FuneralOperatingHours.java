package growthon.withtail_be.domain.animalfuneral.entity;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funeral_operating_hours")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuneralOperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeekType dayOfWeek;

    @Column(nullable = false)
    private boolean closed;

    @Column(length = 10)
    private String openTime;   // "09:30"

    @Column(length = 10)
    private String closeTime;  // "20:30"

    @Builder
    public FuneralOperatingHours(
            AnimalFuneral funeral,
            DayOfWeekType dayOfWeek,
            boolean closed,
            String openTime,
            String closeTime
    ) {
        this.funeral = funeral;
        this.dayOfWeek = dayOfWeek;
        this.closed = closed;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
