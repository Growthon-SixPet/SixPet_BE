package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.domain.user.dto.request.local.LocalSignupReqDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.entity.Provider;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserInfoResDto localSignup(LocalSignupReqDto req) {

        if (userRepository.existsByPhoneNumber(req.phoneNumber())) {
            throw new GeneralException(ErrorStatus.USER_PHONE_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(req.password());

        User user = User.builder()
                .phoneNumber(req.phoneNumber())
                .password(encodedPassword)
                .provider(Provider.LOCAL)
                .providerId(null)
                .name(req.name())
                .nickname(req.nickname())
                .birthDate(req.birthDate())
                .gender(req.gender())
                .address(req.address())
                .profileImage(null)
                .build();

        return UserInfoResDto.from(userRepository.save(user));
    }

}
