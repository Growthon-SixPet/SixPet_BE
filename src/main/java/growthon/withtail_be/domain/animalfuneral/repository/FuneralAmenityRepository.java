package growthon.withtail_be.domain.animalfuneral.repository;

import growthon.withtail_be.domain.animalfuneral.entity.FuneralAmenity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuneralAmenityRepository extends JpaRepository<FuneralAmenity, Long> {

    // 목록 조회에서 한 번에 amenity 이름들 가져오기 (IN 쿼리)
    // funeral까지 접근하려면 funeral도 같이 당겨주는 게 안전
    @EntityGraph(attributePaths = {"amenity", "funeral"})
    List<FuneralAmenity> findByFuneralIdIn(List<Long> funeralIds);

    // 상세 조회에서 해당 장례식장 amenity 가져오기
    @EntityGraph(attributePaths = {"amenity"})
    List<FuneralAmenity> findByFuneralId(Long funeralId);

    // (선택) 필터 검색용: 특정 amenityIds 포함하는 funeralIds 추출 등에 쓸 수도 있음
}
