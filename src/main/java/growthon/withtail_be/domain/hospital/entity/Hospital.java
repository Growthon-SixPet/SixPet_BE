package growthon.withtail_be.domain.hospital.entity;

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

// 병원 테이블(hospitals)과 매핑되는 엔티티
@Entity
@Table(name = "hospitals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
@AllArgsConstructor
@Builder
public class Hospital {

    // PK (auto_increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 병원명 (키워드 검색 대상(
    @Column(nullable = false)
    private String name;

    // 지역 필터용 (시/도, 시/군/구)
    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    // 화면 표시용 주소/연락처
    @Column(nullable = false)
    private String address;

    private String phone;

    // 리스트 카드 표시용(없으면 null/0 가능)
    private Double rating;
    private Integer reviewCount;

    // 24시간 여부
    private Boolean is24h;
}
