package growthon.withtail_be.domain.interest.service;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.TargetType;
import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.repository.FuneralRepository;
import growthon.withtail_be.domain.interest.repository.HospitalRepository;
import growthon.withtail_be.domain.interest.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final HospitalRepository hospitalRepository;
    private final FuneralRepository funeralRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public InterestResDto postInterest(InterestReqDto interestReqDto) {

        Hospital hospital = null;
        Funeral funeral = null;

        if(interestReqDto.getHospitalId() != null) {
            hospital = hospitalRepository.findById(interestReqDto.getHospitalId())
                    .orElseThrow(() -> new RuntimeException("Hospital not found"));
        }

        if(interestReqDto.getFuneralId() != null) {
            funeral = funeralRepository.findById(interestReqDto.getFuneralId())
                    .orElseThrow(() -> new RuntimeException("Funeral not found"));
        }

        Interest interest = new Interest(
                LocalDate.now(),
                interestReqDto.getTargetType(),
                hospital,
                funeral);

        interestRepository.save(interest);

        return InterestResDto.from(interest);
    }

    @Transactional(readOnly = true)
    public List<InterestResDto> getAllInterest() {
        return interestRepository.findAll()
                .stream()
                .map(interest -> InterestResDto.builder()
                        .interestId(interest.getInterestId())
                        .hospitalName(
                                interest.getHospital() != null ? interest.getHospital().getName() : null
                        )
                        .funeralName(
                                interest.getFuneral() != null ? interest.getFuneral().getName() : null
                        )
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InterestResDto> getInterestByTargetType(TargetType targetType) {
        return interestRepository.findByTargetType(targetType)
                .stream()
                .map(interest -> InterestResDto.builder()
                        .interestId(interest.getInterestId())
                        .hospitalName(
                                interest.getHospital() != null ? interest.getHospital().getName() : null
                        )
                        .funeralName(
                                interest.getFuneral() != null ? interest.getFuneral().getName() : null
                        )
                        .build())
                .toList();
    }

    @Transactional
    public void deleteInterest(Long interestId) {

        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("존재하지 않은 즐겨찾기입니다."));

        interestRepository.delete(interest);
    }
}