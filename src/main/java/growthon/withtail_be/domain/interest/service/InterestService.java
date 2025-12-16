package growthon.withtail_be.domain.interest.service;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.interest.domain.Interest;
import growthon.withtail_be.domain.interest.domain.TargetType;
import growthon.withtail_be.domain.interest.domain.User;
import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.repository.FuneralRepository;
import growthon.withtail_be.domain.interest.repository.HospitalRepository;
import growthon.withtail_be.domain.interest.repository.InterestRepository;
import growthon.withtail_be.domain.interest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final HospitalRepository hospitalRepository;
    private final FuneralRepository funeralRepository;
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public InterestResDto postInterest(InterestReqDto interestReqDto) {

        Hospital hospital = null;
        Funeral funeral = null;

        User user = userRepository.findById(interestReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(interestReqDto.getTargetType() == TargetType.HOSPITAL) {
            hospital = hospitalRepository.findById(interestReqDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

            boolean exists = interestRepository.existsByUserAndHospital(user, hospital);
            if (exists) {
                throw new IllegalArgumentException("This interest already exists");
            }
        }

        if(interestReqDto.getTargetType() == TargetType.FUNERAL) {
            funeral = funeralRepository.findById(interestReqDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Funeral not found"));

            boolean exists = interestRepository.existsByUserAndFuneral(user, funeral);
            if (exists) {
                throw new IllegalArgumentException("This interest already exists");
            }
        }

        Interest interest = new Interest(
                LocalDateTime.now(),
                user,
                interestReqDto.getTargetType(),
                hospital,
                funeral);

        interestRepository.save(interest);

        return InterestResDto.from(interest);
    }

    @Transactional(readOnly = true)
    public List<InterestResDto> getAllInterest(Long userId) {
        return interestRepository.findByUserId(userId)
                .stream()
                .map(InterestResDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public InterestResDto getInterest(Long interestId) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));

        return InterestResDto.from(interest);
    }

    @Transactional
    public void deleteInterest(Long interestId) {

        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new IllegalArgumentException("Interest not found"));

        interestRepository.delete(interest);
    }
}