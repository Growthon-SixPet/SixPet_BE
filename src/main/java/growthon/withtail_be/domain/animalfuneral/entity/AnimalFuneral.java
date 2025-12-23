package growthon.withtail_be.domain.animalfuneral.entity;

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
@Table(name = "animal_funerals")
public class AnimalFuneral {

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

    // 소개(상세 기본 상단 문구)
    @Column(columnDefinition = "text")
    private String description;

    // 예약하기 URL(버튼)
    private String reservationUrl;

    // 대표 이미지 (테스트 단계 null 허용)
    private String mainImageUrl;

    // 리뷰 수(장례 검색 정렬/표시용)
    @Column(nullable = false)
    private int reviewCount;

    // 블리스 스톤 제공 여부(필터)
    @Column(nullable = false)
    private boolean blissStoneAvailable;

    // 연관관계
    @OneToMany(mappedBy = "funeral", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralOperatingHours> operatingHours = new ArrayList<>();

    // 주의: 기본 상세에서 operatingHours와 amenities를 동시에 fetch 하면 MultipleBagFetchException 터질 수 있음
    @OneToMany(mappedBy = "funeral", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralAmenity> amenities = new ArrayList<>();

    @OneToMany(mappedBy = "funeral", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralBrandSection> brandSections = new ArrayList<>();

    @OneToMany(mappedBy = "funeral", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralProcedure> procedures = new ArrayList<>();

    @OneToMany(mappedBy = "funeral", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuneralCost> costs = new ArrayList<>();

    protected AnimalFuneral() {
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
    public String getReservationUrl() { return reservationUrl; }
    public String getMainImageUrl() { return mainImageUrl; }
    public int getReviewCount() { return reviewCount; }
    public boolean isBlissStoneAvailable() { return blissStoneAvailable; }

    public List<FuneralOperatingHours> getOperatingHours() { return operatingHours; }
    public List<FuneralAmenity> getAmenities() { return amenities; }
    public List<FuneralBrandSection> getBrandSections() { return brandSections; }
    public List<FuneralProcedure> getProcedures() { return procedures; }
    public List<FuneralCost> getCosts() { return costs; }
}
