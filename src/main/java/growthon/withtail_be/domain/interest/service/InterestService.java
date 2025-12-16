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
    private final HospitalRepository hospitalRepository;
    private final FuneralRepository funeralRepository;
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    // 즐겨찾기 생성
    @Transactional
    public InterestResDto postInterest(InterestReqDto dto, Long userId) {

        if (dto.getTargetType() == null || dto.getTargetId() == null) {
            throw new GeneralException(ErrorStatus.INVALID_INTEREST_TARGET);
        }

        User user = getUserOrThrow(userId);

        Hospital hospital = null;
        Funeral funeral = null;

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

        return InterestResDto.from(interest);
    }

    // 유저별 즐겨찾기 조회
    @Transactional(readOnly = true)
    public List<InterestResDto> findInterestsByUserId(Long userId) {
        getUserOrThrow(userId);

        return interestRepository.findByUserId(userId)
                .stream()
                .map(InterestResDto::from)
                .toList();
    }

    // 즐겨찾기 단건 조회
    @Transactional(readOnly = true)
    public InterestResDto findInterestById(Long interestId, Long userId) {

        Interest interest = getInterestOrThrow(interestId);
        validateOwnerOrThrow(interest, userId);

        return InterestResDto.from(interest);
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

}