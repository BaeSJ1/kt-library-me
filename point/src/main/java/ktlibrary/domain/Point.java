package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.PointApplication;
import ktlibrary.domain.PointDeducted;
import ktlibrary.domain.PointInsufficient;
import lombok.Data;
import org.springframework.context.ApplicationEventPublisher;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private Date createAt;

    @Column(nullable = false)
    private Date updateAt;

    @Column(nullable = false)
    private Long customerId;

    @Column
    private Long price;

    @Column
    private Long bookId;

    //@Column(nullable = false)
    //private Long price;

    /* 
    public static PointRepository repository() {
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }
    */

    // =============================
    // 정적 팩토리 메서드 - 최초 포인트 생성
    // =============================
    public static Point createInitialPoint(CustomerRegistered event) {
        Point point = new Point();
        point.customerId = event.getId();
        point.createAt = new Date();
        point.updateAt = new Date();

        int initPoint = 1000;
        if (Boolean.TRUE.equals(event.getIsKtUser())) {
            initPoint += 5000;
        }

        point.point = (long) initPoint;

        // 도메인 이벤트 등록
        // point.publishAfterCommit(new PointSaved(point));

        return point;
    }


    // =============================
    // 포인트 차감 로직
    // =============================
    public boolean deduct(Long price) {
        if (price == null || price <= 0 || this.point < price) {
            // 차감 실패 이벤트 등록
            // this.publishAfterCommit(new PointInsufficient(this));
            new PointInsufficient(this).publishAfterCommit();
            return false;
        }

        this.point -= price;
        this.updateAt = new Date();
        // 차감 성공 이벤트 등록
        // this.publishAfterCommit(new PointDeducted(this));

        new PointDeducted(this).publishAfterCommit();
        return true;
    }


    // =============================
    // 포인트 적립 로직
    // =============================
    public void requestPoint(Long price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("0보다 큰 포인트만 적립 가능합니다.");
        }

        this.point += price;
        this.updateAt = new Date();

        // 적립 이벤트 등록
        // this.publishAfterCommit(new PointSaved(this));
        new PointSaved(this).publishAfterCommit();
    }

}