package growthon.withtail_be.domain.user.dto.response;

import growthon.withtail_be.domain.user.entity.Gender;
import growthon.withtail_be.domain.user.entity.Provider;
import growthon.withtail_be.domain.user.entity.User;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserInfoResDto(
        Long id,
        String phoneNumber,
        String name,
        String nickname,
        Provider provider,
        LocalDate birthDate,
        Gender gender,
        String address,
        String profileImage
) {
    public static UserInfoResDto from(User user) {
        return UserInfoResDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .address(user.getAddress())
                .profileImage(user.getProfileImage())
                .build();
    }
}
