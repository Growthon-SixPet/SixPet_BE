package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_staff")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicalStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 직급
    private String role;

    // 경력 내역
    @Column(columnDefinition = "text", nullable = false)
    private String careerDescription;

    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    // 의료진-전문분야 매핑 (1:N)
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffSpecialty> staffSpecialties = new ArrayList<>();

    @Builder
    public MedicalStaff(
            String name,
            String role,
            String careerDescription,
            String profileImageUrl,
            AnimalHospital hospital
    ) {
        this.name = name;
        this.role = role;
        this.careerDescription = careerDescription;
        this.profileImageUrl = profileImageUrl;
        this.hospital = hospital;
    }

}
