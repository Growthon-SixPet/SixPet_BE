package growthon.withtail_be.domain.animalhospital.dto;

import growthon.withtail_be.domain.animalhospital.entity.MedicalStaff;
import lombok.Getter;

@Getter
public class AnimalHospitalStaffResponse {

    private final Long id;
    private final String name;
    private final String role;
    private final Integer careerYears;
    private final String education;
    private final String specialtyText;
    private final String profileImageUrl;

    private AnimalHospitalStaffResponse(
            Long id,
            String name,
            String role,
            Integer careerYears,
            String education,
            String specialtyText,
            String profileImageUrl
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.careerYears = careerYears;
        this.education = education;
        this.specialtyText = specialtyText;
        this.profileImageUrl = profileImageUrl;
    }

    public static AnimalHospitalStaffResponse from(MedicalStaff s) {
        return new AnimalHospitalStaffResponse(
                s.getId(),
                s.getName(),
                s.getRole(),
                s.getCareerYears(),
                s.getEducation(),
                s.getSpecialtyText(),
                s.getProfileImageUrl()
        );
    }
}
