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
@Table(name = "funeral_brand_sections")
public class FuneralBrandSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id", nullable = false)
    private AnimalFuneral funeral;

    @Column(nullable = false)
    private String title; // "브랜드 소개 1"

    @Column(columnDefinition = "text", nullable = false)
    private String content; // "간단한 소개 문구..."

    protected FuneralBrandSection() {
    }

    public Long getId() { return id; }
    public AnimalFuneral getFuneral() { return funeral; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}
