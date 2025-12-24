package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "hospital_news")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    @Builder
    public HospitalNews(
            String title,
            String content,
            String imageUrl,
            LocalDate createdAt,
            AnimalHospital hospital
    ) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.hospital = hospital;
    }

}

