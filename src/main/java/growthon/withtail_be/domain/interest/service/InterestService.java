package growthon.withtail_be.domain.interest.service;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralRepository;
import growthon.withtail_be.domain.animalfuneral.service.AnimalFuneralService;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
import growthon.withtail_be.domain.animalhospital.service.AnimalHospitalService;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.TargetType;
import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.repository.InterestRepository;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final AnimalHospitalRepository hospitalRepository;
    private final AnimalFuneralRepository funeralRepository;
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    private final AnimalHospitalService animalHospitalService;
    private final AnimalFuneralService animalFuneralService;


    // 즐겨찾기 생성
    @Transactional
    public InterestResDto postInterest(InterestReqDto dto, Long userId) {

        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
        }

        User user = getUserOrThrow(userId);

        AnimalHospital hospital = null;
        AnimalFuneral funeral = null;

        // 병원일 경우
        if (dto.getTargetType() == TargetType.HOSPITAL) {

            hospital = hospitalRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.HOSPITAL_NOT_FOUND));

            if (interestRepository.existsByUserAndHospital(user, hospital)) {
                throw new GeneralException(ErrorStatus.INTEREST_ALREADY_EXISTS);
            }

        } else if (dto.getTargetType() == TargetType.FUNERAL) {

            funeral = funeralRepository.findById(dto.getTargetId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));

            if (interestRepository.existsByUserAndFuneral(user, funeral)) {
                throw new GeneralException(ErrorStatus.INTEREST_ALREADY_EXISTS);
            }

        } else {
            throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
        }

        Interest interest = new Interest(user, dto.getTargetType(), hospital, funeral);
        interestRepository.save(interest);

        return toResDto(interest);
    }

    // 유저별 즐겨찾기 조회
    @Transactional(readOnly = true)
    public List<InterestResDto> findInterestsByUserId(Long userId) {
        getUserOrThrow(userId);

        return interestRepository.findByUserId(userId)
                .stream()
                .map(this::toResDto)
                .toList();
    }

    // 즐겨찾기 단건 조회
    @Transactional(readOnly = true)
    public InterestResDto findInterestById(Long interestId, Long userId) {

        Interest interest = getInterestOrThrow(interestId);
        validateOwnerOrThrow(interest, userId);

        return toResDto(interest);
    }

    // 즐겨찾기 삭제
    @Transactional
    public void deleteInterest(Long interestId, Long userId) {

        Interest interest = getInterestOrThrow(interestId);
        validateOwnerOrThrow(interest, userId);

        interestRepository.delete(interest);
    }


    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }

    private Interest getInterestOrThrow(Long interestId) {
        return interestRepository.findById(interestId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INTEREST_NOT_FOUND));
    }

    private void validateOwnerOrThrow(Interest interest, Long userId) {
        if (!interest.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.INTEREST_ACCESS_DENIED);
        }
    }

    private InterestResDto toResDto(Interest interest) {

        TargetType type = interest.getTargetType();
        if (type == null) {
            throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
        }

        if (type == TargetType.HOSPITAL) {
            AnimalHospital h = interest.getHospital();
            if (h == null) {
                throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
            }

            boolean openNow = animalHospitalService.calculateOpenNow(h);
            return InterestResDto.fromHospital(interest, h, openNow);
        }

        if (type == TargetType.FUNERAL) {
            AnimalFuneral f = interest.getFuneral();
            if (f == null) {
                throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
            }

            boolean openNow = animalFuneralService.calculateOpenNow(f);
            return InterestResDto.fromFuneral(interest, f, openNow);
        }

        // TargetType에 값이 추가되거나 예상 못한 값이 들어온 경우
        throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
    }

}