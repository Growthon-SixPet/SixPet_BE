package growthon.withtail_be.domain.user.entity;

import growthon.withtail_be.domain.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;   // LOCAL / GOOGLE / KAKAO / NAVER

    private String providerId;
    private String name;
    private String nickname;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;
    private String profileImage;

    @Builder
    public User(String phoneNumber, String password, Provider provider,
                String providerId, String name, String nickname,
                LocalDate birthDate, Gender gender,
                String address, String profileImage) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.profileImage = profileImage;
    }
}
