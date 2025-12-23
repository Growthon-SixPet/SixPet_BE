package growthon.withtail_be.domain.animalhospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "hospital_specialties",
        uniqueConstraints = @UniqueConstraint(name = "uk_hospital_specialty", columnNames = {"hospital_id", "specialty_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Builder
    public HospitalSpecialty(AnimalHospital hospital, Specialty specialty) {
        this.hospital = hospital;
        this.specialty = specialty;
    }

}
