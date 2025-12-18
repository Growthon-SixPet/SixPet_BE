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

@Entity
@Table(name = "medical_staff")
public class MedicalStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름/직책/경력/학력/전문문구/이미지
    @Column(nullable = false)
    private String name;

    private String role;

    private Integer careerYears;

    @Column(columnDefinition = "text")
    private String education;

    private String specialtyText;

    private String profileImageUrl; // 테스트 단계: null 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    protected MedicalStaff() {
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public Integer getCareerYears() { return careerYears; }
    public String getEducation() { return education; }
    public String getSpecialtyText() { return specialtyText; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public AnimalHospital getHospital() { return hospital; }
}
