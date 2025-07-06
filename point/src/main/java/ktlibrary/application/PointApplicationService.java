package ktlibrary.application;

import ktlibrary.domain.*;
import ktlibrary.domain.RequestPointCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Point 도메인의 Application Service
 * 역할: 이벤트 수신 후 비즈니스 흐름을 조율하고, 도메인 객체 호출 및 이벤트 발행 담당
 */
@Service
@Transactional
public class PointApplicationService {

    @Autowired
    private PointRepository pointRepository;

    // 신규 가입 시
    public void addPoint(CustomerRegistered event) {
        System.out.println("\n\n##### [CustomerApplicationService] addPoint : " + event + "\n\n");
        Point point = Point.createInitialPoint(event);
        pointRepository.save(point);
        new PointSaved(point).publishAfterCommit();
    }

    // 포인트 차감
    public boolean  deductPoint(InvalidSubscription event) {
        Point point = pointRepository.findByCustomerId(event.getCustomerId())
            .orElseThrow(() -> new IllegalStateException("포인트 정보가 없습니다."));

        // 책 ID 및 가격 정보 저장
        point.setBookId(event.getBookId());
        point.setPrice(event.getPrice());

        // 실제 포인트 차감 시도
        boolean success = point.deduct(event.getPrice());

        if (!success) {
            // 포인트 부족 → 저장하지 않고 종료
            System.out.println("포인트 부족으로 차감 실패: customerId = " + event.getCustomerId());
            return false;
        }

        // 차감 성공한 경우에만 저장
        pointRepository.save(point);
        return true;
    }

    // 포인트 요청 처리
    public void requestPoint(RequestPointCommand command) {
        Point point = pointRepository.findByCustomerId(command.getCustomerId())
            .orElseThrow(() -> new IllegalStateException("포인트 정보가 없습니다."));

        point.requestPoint(command.getPoint());
        pointRepository.save(point);
    }

    public boolean processInvalidSubscription(InvalidSubscription event) {
        return deductPoint(event);
    }

    public void processCustomerRegistration(CustomerRegistered event) {
        addPoint(event);
    }
}