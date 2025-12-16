package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "animal_hospitals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    // 주소
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;

    @Column(nullable = false)
    private String shortAddress;

    // 연락처/소개
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 운영
    @Column(nullable = false)
    private boolean open24h;

    @Column(nullable = false)
    private boolean emergencyAvailable;

    // 평점/리뷰수 (임시값)
    private Double rating;
    private Integer reviewCount;

    // 대표 이미지 (검색 카드)
    private String mainImageUrl;

    // 1:N - 이미지/운영시간/의료진
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private final List<HospitalImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayOfWeek ASC")
    private final List<HospitalOperatingHours> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MedicalStaff> staff = new ArrayList<>();

    // N:M - 조인 엔티티
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HospitalSpecialty> hospitalSpecialties = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HospitalAnimalType> hospitalAnimalTypes = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HospitalAmenity> hospitalAmenities = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HospitalPaymentMethod> hospitalPaymentMethods = new ArrayList<>();
}
