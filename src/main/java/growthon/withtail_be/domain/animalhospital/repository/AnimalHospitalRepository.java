package growthon.withtail_be.domain.animalhospital.repository;

import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnimalHospitalRepository
        extends JpaRepository<AnimalHospital, Long>, JpaSpecificationExecutor<AnimalHospital> {

    // 상세(기본): 이미지 + 운영시간 + 전문분야/동물종(검색 배지/기본정보용)
    @EntityGraph(attributePaths = {
            "images",
            "operatingHours",
            "hospitalSpecialties",
            "hospitalSpecialties.specialty"
    })
    Optional<AnimalHospital> findWithDetailById(Long id);

    // 소개 탭: 소개 + 서비스/결제수단
    @EntityGraph(attributePaths = {
            "hospitalAmenities",
            "hospitalAmenities.amenity",
            "hospitalPaymentMethods",
            "hospitalPaymentMethods.paymentMethod"
    })
    Optional<AnimalHospital> findWithAboutById(Long id);

    // 의료진 탭: staff만
    @EntityGraph(attributePaths = {"staff"})
    Optional<AnimalHospital> findWithStaffById(Long id);
}
