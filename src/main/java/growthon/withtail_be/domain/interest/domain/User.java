package growthon.withtail_be.domain.interest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
