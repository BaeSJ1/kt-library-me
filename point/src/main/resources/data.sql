-- 고객 ID 1번에 대해 포인트 데이터 삽입 (초기 6,000P)
INSERT INTO point_table (
    id,              -- PK (Point Entity에서 @GeneratedValue가 아니라면 반드시 지정)
    customer_id,     -- 고객 ID (FK 아님, 일반 컬럼)
    point,           -- 보유 포인트
    book_id,         -- null 가능
    price,           -- null 가능
    create_at,
    update_at
)
VALUES (
     1,               -- ID (직접 지정)
     1,               -- customer_id (더미 고객)
     6000,            -- 초기 포인트
     NULL,
     NULL,
     CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);