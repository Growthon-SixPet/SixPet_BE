package growthon.withtail_be.domain.animalfuneral.entity;

import growthon.withtail_be.domain.model.Amenity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funeral_amenities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuneralAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    @Builder
    public FuneralAmenity(AnimalFuneral funeral, Amenity amenity) {
        this.funeral = funeral;
        this.amenity = amenity;
    }
}
