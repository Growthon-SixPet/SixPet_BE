package growthon.withtail_be.domain.animalfuneral.entity;

import growthon.withtail_be.domain.model.RegionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animal_funerals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalFuneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본 정보
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RegionType region;

    @Column(nullable = false, length = 255)
    private String address;

    private String phone;

    // 소개(상세 기본 상단 문구)
    @Column(columnDefinition = "text")
    private String description;

    // 대표 이미지 (테스트 단계 null 허용)
    private String mainImageUrl;

    @Column(name = "min_cost", nullable = false)
    private int minCost;

    @Column(nullable = false)
    private boolean blissStoneAvailable;

    private String procedureImageUrl;   // 장례 절차 이미지
    private String blissStoneImageUrl; // 메모리얼 스톤 설명 이미지

    // 리뷰 수(장례 검색 정렬/표시용)
    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private double ratingAvg;

    // 연관관계

    // 운영시간 (1:N)
    @OneToMany(mappedBy = "funeral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralOperatingHours> operatingHours = new ArrayList<>();

    // 장례 비용/패키지 (1:N)
    @OneToMany(mappedBy = "funeral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralCost> costs = new ArrayList<>();

    // 장례식장-편의시설 (N:M 조인 엔티티)
    @OneToMany(mappedBy = "funeral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralAmenity> amenities = new ArrayList<>();

    @Builder
    public AnimalFuneral(
            String name,
            RegionType region,
            String address,
            String phone,
            String description,
            String mainImageUrl,
            int minCost,
            boolean blissStoneAvailable,
            String procedureImageUrl,
            String blissStoneImageUrl,
            int reviewCount,
            double ratingAvg
    ) {
        this.name = name;
        this.region = region;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.minCost = minCost;
        this.blissStoneAvailable = blissStoneAvailable;
        this.procedureImageUrl = procedureImageUrl;
        this.blissStoneImageUrl = blissStoneImageUrl;
        this.reviewCount = reviewCount;
        this.ratingAvg = ratingAvg;
    }

}
