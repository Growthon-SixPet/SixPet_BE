package growthon.withtail_be.domain.user.service;

import growthon.withtail_be.domain.user.dto.request.create.local.LocalSignupReqDto;
import growthon.withtail_be.domain.user.dto.request.update.PasswordUpdateReqDto;
import growthon.withtail_be.domain.user.dto.request.update.UserProfileUpdateReqDto;
import growthon.withtail_be.domain.user.dto.response.UserInfoResDto;
import growthon.withtail_be.domain.user.entity.Provider;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.RefreshTokenRepository;
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
    private final PhoneAuthService phoneAuthService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    public UserInfoResDto localSignup(LocalSignupReqDto req) {

        // 휴대폰 인증 완료 여부 체크
        phoneAuthService.validateVerifiedPhone(req.phoneNumber());

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

        User savedUser = userRepository.save(user);

        // 회원가입 성공 후 인증완료 키 삭제
        phoneAuthService.clearVerifiedPhone(req.phoneNumber());

        return UserInfoResDto.from(savedUser);
    }

    // 회원 정보 단건 조회
    @Transactional(readOnly = true)
    public UserInfoResDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return UserInfoResDto.from(user);
    }

    // 회원 기본정보 수정 (이름/닉네임/생년월일/성별/주소)
    public UserInfoResDto updateProfile(Long userId, UserProfileUpdateReqDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateProfile(
                req.name(),
                req.nickname(),
                req.birthDate(),
                req.gender(),
                req.address()
        );

        return UserInfoResDto.from(user);
    }

    // 비밀번호 변경 (LOCAL만 가능 + 현재 비번 검증 + 확인 일치)
    public void updatePassword(Long userId, PasswordUpdateReqDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 소셜 유저는 비밀번호 변경 불가
        if (user.getProvider() != Provider.LOCAL) {
            throw new GeneralException(ErrorStatus.AUTH_PROVIDER_MISMATCH);
        }

        // 현재 비밀번호 검증
        if (user.getPassword() == null || !passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
        }

        // 새 비밀번호 확인 일치 체크
        if (!req.newPassword().equals(req.newPasswordConfirm())) {
            throw new GeneralException(ErrorStatus.PASSWORD_CONFIRM_MISMATCH);
        }

        String encodedNewPassword = passwordEncoder.encode(req.newPassword());
        user.updatePassword(encodedNewPassword);
    }

    // 회원 탈퇴
    public void deleteUser(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

}
