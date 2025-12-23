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

@Entity
@Table(name = "funeral_costs")
public class FuneralCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Column(nullable = false)
    private String name; // "베이직 장례(소형)"

    @Column(nullable = false)
    private int price; // 350000

    protected FuneralCost() {
    }

    public Long getId() { return id; }
    public AnimalFuneral getFuneral() { return funeral; }
    public String getName() { return name; }
    public int getPrice() { return price; }
}
