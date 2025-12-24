package growthon.withtail_be.domain.reservation.service;

import growthon.withtail_be.domain.animalfuneral.entity.AnimalFuneral;
import growthon.withtail_be.domain.animalfuneral.repository.AnimalFuneralRepository;
import growthon.withtail_be.domain.animalfuneral.service.AnimalFuneralService;
import growthon.withtail_be.domain.animalhospital.entity.AnimalHospital;
import growthon.withtail_be.domain.animalhospital.repository.AnimalHospitalRepository;
import growthon.withtail_be.domain.animalhospital.service.AnimalHospitalService;
import growthon.withtail_be.domain.reservation.domain.Reservation;
import growthon.withtail_be.domain.reservation.domain.ReservationStatus;
import growthon.withtail_be.domain.reservation.domain.TargetType;
import growthon.withtail_be.domain.reservation.dto.ReservationReqDto;
import growthon.withtail_be.domain.reservation.dto.ReservationResDto;
import growthon.withtail_be.domain.reservation.repository.ReservationRepository;
import growthon.withtail_be.domain.user.entity.User;
import growthon.withtail_be.domain.user.repository.UserRepository;
import growthon.withtail_be.global.code.ErrorStatus;
import growthon.withtail_be.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AnimalHospitalRepository hospitalRepository;
    private final AnimalFuneralRepository funeralRepository;

    private final AnimalHospitalService animalHospitalService;
    private final AnimalFuneralService animalFuneralService;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter RES_NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // 예약 생성
    @Transactional
    public ReservationResDto postReservation(Long userId, TargetType targetType, Long targetId, ReservationReqDto dto) {
        User user = getUserOrThrow(userId);

        AnimalHospital hospital = null;
        AnimalFuneral funeral = null;

        if (targetType == TargetType.HOSPITAL) {
            hospital = getHospitalOrThrow(targetId);

            boolean conflict = reservationRepository
                    .existsByHospitalAndReservationDateAndReservationTimeAndStatusNot(
                            hospital,
                            dto.getReservationDate(),
                            dto.getReservationTime(),
                            ReservationStatus.CANCELED
                    );

            if (conflict) {
                throwTimeConflict();
            }

        } else if (targetType == TargetType.FUNERAL) {
            funeral = getFuneralOrThrow(targetId);

            boolean conflict = reservationRepository
                    .existsByFuneralAndReservationDateAndReservationTimeAndStatusNot(
                            funeral,
                            dto.getReservationDate(),
                            dto.getReservationTime(),
                            ReservationStatus.CANCELED
                    );

            if (conflict) {
                throwTimeConflict();
            }

        } else {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_TARGET);
        }

        // 3) 예약번호 생성 (중복 방지 위해 존재 여부 체크하며 몇 번 재시도)
        String reservationNumber = generateReservationNumber();
        int retry = 0;
        while (reservationRepository.existsByReservationNumber(reservationNumber)) {
            if (++retry >= 5) throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
            reservationNumber = generateReservationNumber();
        }

        // 4) 예약 생성 (보호자명/연락처는 예약 엔티티에 스냅샷 저장)
        Reservation reservation = new Reservation(
                reservationNumber,
                user,
                targetType,
                hospital,
                funeral,
                dto.getOwnerName(),
                dto.getPhoneNumber(),
                dto.getPetName(),
                dto.getReservationDate(),
                dto.getReservationTime(),
                dto.getVisitReason()
        );

        reservationRepository.save(reservation);

        Reservation saved = reservationRepository.findWithTargetsById(reservation.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR));

        return ReservationResDto.from(saved, calculateOpenNow(saved));
    }


    // 유저별 예약 내역 조회
    @Transactional(readOnly = true)
    public List<ReservationResDto> findByUserId(Long userId) {
        getUserOrThrow(userId);

        return reservationRepository.findAllByUser_Id(userId)
                .stream()
                .map(r -> ReservationResDto.from(r, calculateOpenNow(r)))
                .toList();
    }

    // 예약 단건 조회
    @Transactional(readOnly = true)
    public ReservationResDto getReservation(Long reservationId, Long userId) {
        Reservation reservation = getReservationOrThrow(reservationId);

        validateReservationOwnerOrThrow(reservation, userId);

        return ReservationResDto.from(reservation, calculateOpenNow(reservation));
    }

    // 예약 변경
    @Transactional
    public ReservationResDto updateReservation(
            Long reservationId,
            Long userId,
            ReservationReqDto dto
    ) {
        Reservation reservation = getReservationOrThrow(reservationId);

        validateReservationOwnerOrThrow(reservation, userId);

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_CANCELED);
        }

        TargetType targetType = reservation.getTargetType();
        AnimalHospital hospital = reservation.getHospital();
        AnimalFuneral funeral = reservation.getFuneral();

        if (targetType == TargetType.HOSPITAL) {
            if (hospital == null) throw new GeneralException(ErrorStatus.INVALID_RESERVATION_TARGET);

            boolean conflict = reservationRepository
                    .existsByHospitalAndReservationDateAndReservationTimeAndStatusNotAndIdNot(
                            hospital,
                            dto.getReservationDate(),
                            dto.getReservationTime(),
                            ReservationStatus.CANCELED,
                            reservation.getId()
                    );

            if (conflict) {
                throwTimeConflict();
            }

        } else if (targetType == TargetType.FUNERAL) {
            if (funeral == null) throw new GeneralException(ErrorStatus.INVALID_RESERVATION_TARGET);

            boolean conflict = reservationRepository
                    .existsByFuneralAndReservationDateAndReservationTimeAndStatusNotAndIdNot(
                            funeral,
                            dto.getReservationDate(),
                            dto.getReservationTime(),
                            ReservationStatus.CANCELED,
                            reservation.getId()
                    );

            if (conflict) {
                throwTimeConflict();
            }

        } else {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_TARGET);
        }

        reservation.update(dto);

        return ReservationResDto.from(reservation, calculateOpenNow(reservation));
    }

    // 예약 취소
    @Transactional
    public ReservationResDto cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = getReservationOrThrow(reservationId);

        validateReservationOwnerOrThrow(reservation, userId);

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_CANCELED);
        }

        reservation.changeStatus(ReservationStatus.CANCELED);

        return ReservationResDto.from(reservation, calculateOpenNow(reservation));
    }


    // helpers
    private String generateReservationNumber() {
        // 예: R20251217041322-483920
        String ts = LocalDateTime.now().format(RES_NO_FMT);
        int rand = 100000 + RANDOM.nextInt(900000);
        return "R" + ts + "-" + rand;
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
    }


    private Reservation getReservationOrThrow(Long reservationId) {
        return reservationRepository.findWithTargetsById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));
    }

    private void validateReservationOwnerOrThrow(Reservation reservation, Long userId) {
        if (!reservation.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.RESERVATION_ACCESS_DENIED);
        }
    }

    private AnimalHospital getHospitalOrThrow(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.HOSPITAL_NOT_FOUND));
    }

    private AnimalFuneral getFuneralOrThrow(Long funeralId) {
        return funeralRepository.findById(funeralId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));
    }

    private void throwTimeConflict() {
        throw new GeneralException(ErrorStatus.RESERVATION_TIME_CONFLICT);
    }

    private boolean calculateOpenNow(Reservation reservation) {
        if (reservation.getTargetType() == TargetType.HOSPITAL) {
            AnimalHospital hospital = reservation.getHospital();
            return hospital != null && animalHospitalService.calculateOpenNow(hospital);
        }
        if (reservation.getTargetType() == TargetType.FUNERAL) {
            AnimalFuneral funeral = reservation.getFuneral();
            return funeral != null && animalFuneralService.calculateOpenNow(funeral);
        }
        return false;
    }

}
