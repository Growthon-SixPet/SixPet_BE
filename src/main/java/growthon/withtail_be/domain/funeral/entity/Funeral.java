package growthon.withtail_be.domain.funeral.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funerals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Funeral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 업체명
    @Column(nullable = false)
    private String name;

    // 지역(시/도, 시/군/구)
    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    // 주소/연락처
    @Column(nullable = false)
    private String address;

    private String phone;

    // 리스트 표시용
    private Double rating;
    private Integer reviewCount;

    // 가격대(임시): 최저/최고
    private Integer minPrice;
    private Integer maxPrice;
}
