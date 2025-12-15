package growthon.withtail_be.domain.interest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Funeral {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    public Funeral(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
