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

@Entity
@Table(name = "hospital_operating_hours")
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

    @Column(nullable = false)
    private boolean closed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    protected HospitalOperatingHours() {
    }

    public Long getId() { return id; }
    public DayOfWeekType getDayOfWeek() { return dayOfWeek; }
    public String getOpenTime() { return openTime; }
    public String getCloseTime() { return closeTime; }
    public boolean isClosed() { return closed; }
    public AnimalHospital getHospital() { return hospital; }
}
