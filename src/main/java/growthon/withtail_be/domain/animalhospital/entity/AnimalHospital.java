package growthon.withtail_be.domain.animalhospital.entity;

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
@Table(name = "animal_hospitals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalHospital {

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

    @Column(length = 30)
    private String phone;

    @Column(columnDefinition = "text")
    private String description;

    // 운영 필터

    @Column(nullable = false)
    private boolean open24h;

    @Column(nullable = false)
    private boolean nightCare;

    // 대표 이미지
    private String mainImageUrl;

    // 정렬 필터
    @Column(nullable = false)
    private double ratingAvg;

    @Column(nullable = false)
    private int reviewCount;

    // 연관관계

    // 진료시간 (1:N)
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalOperatingHours> operatingHours = new ArrayList<>();

    // 병원 소식 (1:N)
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalNews> newsList = new ArrayList<>();

    // 의료진 (1:N)
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalStaff> staffList = new ArrayList<>();

    // 병원-전문분야 (N:M 조인 엔티티)
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalSpecialty> specialties = new ArrayList<>();

    // 병원-동물종 (N:M 조인 엔티티)
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalAnimalType> animalTypes = new ArrayList<>();

    // 병원-편의시설 (N:M 조인 엔티티)
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalAmenity> amenities = new ArrayList<>();

    @Builder
    public AnimalHospital(
            String name,
            RegionType region,
            String address,
            String phone,
            String description,
            boolean open24h,
            boolean nightCare,
            String mainImageUrl,
            double ratingAvg,
            int reviewCount
    ) {
        this.name = name;
        this.region = region;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.open24h = open24h;
        this.nightCare = nightCare;
        this.mainImageUrl = mainImageUrl;
        this.ratingAvg = ratingAvg;
        this.reviewCount = reviewCount;
    }

    public void updateReviewStats(double ratingAvg, int reviewCount) {
        this.ratingAvg = ratingAvg;
        this.reviewCount = reviewCount;
    }

}
