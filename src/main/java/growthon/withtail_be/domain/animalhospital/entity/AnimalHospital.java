package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animal_hospitals")
public class AnimalHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기본 정보
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;

    @Column(nullable = false)
    private String shortAddress;

    private String phone;

    // 소개
    @Column(columnDefinition = "text")
    private String description;

    // 운영
    @Column(nullable = false)
    private boolean open24h;

    @Column(nullable = false)
    private boolean nightCare;

    @Column(nullable = false)
    private boolean emergencyAvailable;

    // 평점/후기수 (검색 정렬용)
    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private int reviewCount;

    // 대표 이미지 (테스트 단계 null 허용)
    private String mainImageUrl;

    // 연관관계
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalOperatingHours> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalStaff> staff = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalSpecialty> specialties = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalAnimalType> animalTypes = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalAmenity> amenities = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalPaymentMethod> paymentMethods = new ArrayList<>();

    protected AnimalHospital() {
    }

    // --- getter ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSido() { return sido; }
    public String getSigungu() { return sigungu; }
    public String getRoadAddress() { return roadAddress; }
    public String getJibunAddress() { return jibunAddress; }
    public String getDetailAddress() { return detailAddress; }
    public String getShortAddress() { return shortAddress; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public boolean isOpen24h() { return open24h; }
    public boolean isNightCare() { return nightCare; }
    public boolean isEmergencyAvailable() { return emergencyAvailable; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getMainImageUrl() { return mainImageUrl; }

    public List<HospitalImage> getImages() { return images; }
    public List<HospitalOperatingHours> getOperatingHours() { return operatingHours; }
    public List<MedicalStaff> getStaff() { return staff; }
    public List<HospitalSpecialty> getSpecialties() { return specialties; }
    public List<HospitalAnimalType> getAnimalTypes() { return animalTypes; }
    public List<HospitalAmenity> getAmenities() { return amenities; }
    public List<HospitalPaymentMethod> getPaymentMethods() { return paymentMethods; }
}
