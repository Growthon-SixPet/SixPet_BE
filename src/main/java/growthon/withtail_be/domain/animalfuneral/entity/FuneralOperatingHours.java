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

@Entity
@Table(name = "funeral_operating_hours")
public class FuneralOperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeekType dayOfWeek;

    @Column(nullable = false)
    private String openTime;   // "09:30"

    @Column(nullable = false)
    private String closeTime;  // "20:30"

    // 예: "11:30-12:00" (없으면 null)
    private String breakTime;

    // 예: "20:00 접수마감" 같은 문구(없으면 null)
    private String note;

    protected FuneralOperatingHours() {
    }

    public Long getId() { return id; }
    public AnimalFuneral getFuneral() { return funeral; }
    public DayOfWeekType getDayOfWeek() { return dayOfWeek; }
    public String getOpenTime() { return openTime; }
    public String getCloseTime() { return closeTime; }
    public String getBreakTime() { return breakTime; }
    public String getNote() { return note; }
}
