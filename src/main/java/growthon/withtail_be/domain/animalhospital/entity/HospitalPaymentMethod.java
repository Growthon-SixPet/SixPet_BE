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

@Entity
@Table(
        name = "hospital_payment_methods",
        uniqueConstraints = @UniqueConstraint(name = "uk_hospital_payment_method", columnNames = {"hospital_id", "payment_method_id"})
)
public class HospitalPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private AnimalHospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    protected HospitalPaymentMethod() {
    }

    public Long getId() { return id; }
    public AnimalHospital getHospital() { return hospital; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
}
