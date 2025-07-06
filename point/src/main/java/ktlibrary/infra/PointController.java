package ktlibrary.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}