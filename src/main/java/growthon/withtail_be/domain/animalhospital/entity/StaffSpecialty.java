package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "staff_specialties"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 의료진 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private MedicalStaff staff;

    // 전문분야 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Builder
    public StaffSpecialty(MedicalStaff staff, Specialty specialty) {
        this.staff = staff;
        this.specialty = specialty;
    }
}
