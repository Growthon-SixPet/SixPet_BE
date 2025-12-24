package growthon.withtail_be.domain.interest.domain;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.model.BaseEntity;
import growthon.withtail_be.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private AnimalHospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funeral_id")
    private AnimalFuneral funeral;

    public Interest(User user, TargetType targetType,
                    AnimalHospital hospital, AnimalFuneral funeral) {
        this.user = user;
        this.targetType = targetType;
        this.hospital = hospital;
        this.funeral = funeral;
    }

}
