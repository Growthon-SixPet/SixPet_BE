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
@Table(name = "hospital_animal_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalAnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_type_id", nullable = false)
    private AnimalType animalType;

    @Builder
    public HospitalAnimalType(AnimalHospital hospital, AnimalType animalType) {
        this.hospital = hospital;
        this.animalType = animalType;
    }
}
