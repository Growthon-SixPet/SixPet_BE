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
@Table(name = "funeral_procedures")
public class FuneralProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Column(nullable = false)
    private int stepNo; // 1,2,3...

    @Column(nullable = false)
    private String title; // "장례절차 1"

    @Column(columnDefinition = "text", nullable = false)
    private String description; // "절차 설명..."

    protected FuneralProcedure() {
    }

    public Long getId() { return id; }
    public AnimalFuneral getFuneral() { return funeral; }
    public int getStepNo() { return stepNo; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
