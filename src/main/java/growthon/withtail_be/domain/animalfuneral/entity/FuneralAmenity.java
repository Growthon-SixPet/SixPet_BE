package growthon.withtail_be.domain.animalfuneral.entity;

import growthon.withtail_be.domain.model.Amenity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funeral_amenities")
public class FuneralAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1 (링크 -> 장례식장)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    // N:1 (링크 -> 제공 서비스 마스터)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    protected FuneralAmenity() {
    }

    public Long getId() { return id; }
    public AnimalFuneral getFuneral() { return funeral; }
    public Amenity getAmenity() { return amenity; }
}
