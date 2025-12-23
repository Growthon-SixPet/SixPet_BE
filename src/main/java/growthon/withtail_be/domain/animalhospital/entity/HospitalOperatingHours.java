package growthon.withtail_be.domain.animalhospital.entity;

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
@Table(name = "hospital_operating_hours")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalOperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeekType dayOfWeek;

    // "09:00" 같은 문자열로도 충분 (테스트 단계)
    private String openTime;
    private String closeTime;

    // 휴무 여부
    @Column(nullable = false)
    private boolean closed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    @Builder
    public HospitalOperatingHours(
            DayOfWeekType dayOfWeek,
            String openTime,
            String closeTime,
            boolean closed,
            AnimalHospital hospital
    ) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.closed = closed;
        this.hospital = hospital;
    }

}
