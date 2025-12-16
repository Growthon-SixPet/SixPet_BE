package growthon.withtail_be.domain.reservation.service;

import growthon.withtail_be.domain.interest.domain.Funeral;
import growthon.withtail_be.domain.interest.domain.Hospital;
import growthon.withtail_be.domain.interest.repository.FuneralRepository;
import growthon.withtail_be.domain.interest.repository.HospitalRepository;
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
    private final HospitalRepository hospitalRepository;
    private final FuneralRepository funeralRepository;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final DateTimeFormatter RES_NO_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // 예약 생성
    @Transactional
    public ReservationResDto postReservation(Long userId, ReservationReqDto dto) {
        User user = getUserOrThrow(userId);

        // 2) targetType에 따라 대상 엔티티 조회 + 시간 중복 체크
        TargetType targetType = dto.getTargetType();

        Hospital hospital = null;
        Funeral funeral = null;

        if (targetType == TargetType.HOSPITAL) {
            hospital = getHospitalOrThrow(dto.getTargetId());

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
            funeral = getFuneralOrThrow(dto.getTargetId());

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

        return ReservationResDto.from(reservation);
    }


    // 유저별 예약 내역 조회
    @Transactional(readOnly = true)
    public List<ReservationResDto> findByUserId(Long userId) {
        getUserOrThrow(userId);

        return reservationRepository.findAllByUser_Id(userId)
                .stream()
                .map(ReservationResDto::from)
                .toList();
    }

    // 예약 단건 조회
    @Transactional(readOnly = true)
    public ReservationResDto getReservation(Long reservationId, Long userId) {
        Reservation reservation = getReservationOrThrow(reservationId);

        validateReservationOwnerOrThrow(reservation, userId);

        return ReservationResDto.from(reservation);
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

        TargetType targetType = dto.getTargetType();
        Hospital hospital = null;
        Funeral funeral = null;

        if (targetType == TargetType.HOSPITAL) {
            hospital = getHospitalOrThrow(dto.getTargetId());

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
            funeral = getFuneralOrThrow(dto.getTargetId());

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

        reservation.update(
                targetType,
                hospital,
                funeral,
                dto
        );

        return ReservationResDto.from(reservation);
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

        return ReservationResDto.from(reservation);
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
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));
    }

    private void validateReservationOwnerOrThrow(Reservation reservation, Long userId) {
        if (!reservation.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.RESERVATION_ACCESS_DENIED);
        }
    }

    private Hospital getHospitalOrThrow(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.HOSPITAL_NOT_FOUND));
    }

    private Funeral getFuneralOrThrow(Long funeralId) {
        return funeralRepository.findById(funeralId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FUNERAL_NOT_FOUND));
    }

    private void throwTimeConflict() {
        throw new GeneralException(ErrorStatus.RESERVATION_TIME_CONFLICT);
    }

}
