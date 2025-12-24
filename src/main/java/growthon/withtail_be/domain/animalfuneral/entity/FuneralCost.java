package growthon.withtail_be.domain.animalfuneral.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funeral_costs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FuneralCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Builder
    public FuneralCost(
            AnimalFuneral funeral,
            String name,
            int price
    ) {
        this.funeral = funeral;
        this.name = name;
        this.price = price;
    }
}
