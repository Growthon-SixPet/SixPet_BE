package growthon.withtail_be.domain.animalhospital.dto.response;

import growthon.withtail_be.domain.animalhospital.entity.MedicalStaff;
import growthon.withtail_be.domain.animalhospital.entity.StaffSpecialty;

import java.util.List;

public record MedicalStaffResDto(
        Long staffId,
        String name,
        String role,
        String careerDescription,
        String profileImageUrl,
        List<String> specialties
) {

    public static MedicalStaffResDto from(MedicalStaff staff) {
        List<String> specialties = staff.getStaffSpecialties().stream()
                .map(StaffSpecialty::getSpecialty)
                .map(s -> s.getName())
                .toList();

        return new MedicalStaffResDto(
                staff.getId(),
                staff.getName(),
                staff.getRole(),
                staff.getCareerDescription(),
                staff.getProfileImageUrl(),
                specialties
        );
    }
}

