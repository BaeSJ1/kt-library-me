package ktlibrary.infra;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/points")
@Transactional
public class PointController {

    @Autowired
    PointRepository pointRepository;

    @RequestMapping(
        value = "/points/requestpoint",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Point requestPoint(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestPointCommand requestPointCommand
    ) throws Exception {
        System.out.println("##### /point/requestPoint  called #####");

        // 기존 포인트를 DB에서 조회
        Point point = pointRepository.findByCustomerId(requestPointCommand.getCustomerId())
            .orElseThrow(() -> new IllegalStateException("포인트 정보가 없습니다."));

        // Long 값만 넘김
        point.requestPoint(requestPointCommand.getPoint());

        pointRepository.save(point);
        return point;
    }

    @GetMapping("/points/{customerId}")
    public Point getPointByCustomerId(@PathVariable Long customerId) {
        return pointRepository.findByCustomerId(customerId)
            .orElseThrow(() -> new IllegalStateException("포인트 정보가 없습니다."));
    }

    @PostMapping("/points/deduct")
    public ResponseEntity<?> deductPoint(@RequestBody RequestPointCommand command) {
        Point point = pointRepository.findByCustomerId(command.getCustomerId())
            .orElseThrow(() -> new IllegalStateException("포인트 정보 없음"));

        boolean success = point.deduct(command.getPoint());

        if (success) {
            pointRepository.save(point);
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "포인트 차감 성공"
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "status", "POINT_INSUFFICIENT",
                "message", "포인트가 부족합니다"
            ));
        }
    }

}