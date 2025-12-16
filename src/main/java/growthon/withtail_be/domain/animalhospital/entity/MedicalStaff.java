package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical_staff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicalStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: "김수의"
    @Column(nullable = false)
    private String name;

    // 예: "원장", "수의사"  (position 대신 role 사용)
    @Column(nullable = false)
    private String role;

    @Column(name = "career_years")
    private Integer careerYears;

    // 예: "서울대 수의대 ..."
    private String education;

    // 예: "내과/외과"
    @Column(name = "specialty_text")
    private String specialtyText;

    // 테스트 단계에서는 null 가능
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;
}
