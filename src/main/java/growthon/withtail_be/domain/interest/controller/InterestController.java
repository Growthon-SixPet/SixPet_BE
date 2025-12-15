package growthon.withtail_be.domain.interest.controller;

import growthon.withtail_be.domain.interest.domain.TargetType;
import growthon.withtail_be.domain.interest.dto.InterestReqDto;
import growthon.withtail_be.domain.interest.dto.InterestResDto;
import growthon.withtail_be.domain.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestService interestService;

    @PostMapping
    public ResponseEntity<InterestResDto> postInterest(@RequestBody InterestReqDto interestReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interestService.postInterest(interestReqDto));
    }

    @GetMapping
    public ResponseEntity<List<InterestResDto>> getAllInterest() {
        return ResponseEntity.status(HttpStatus.OK).body(interestService.getAllInterest());
    }

    @GetMapping("/{targetType}")
    public ResponseEntity<List<InterestResDto>> getInterestByTargetType(@PathVariable TargetType targetType) {
        return ResponseEntity.status(HttpStatus.OK).body(interestService.getInterestByTargetType(targetType));
    }

    @DeleteMapping("/{interest-id}")
    public ResponseEntity<InterestResDto> deleteInterest(@PathVariable Long interestId) {
        interestService.deleteInterest(interestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
