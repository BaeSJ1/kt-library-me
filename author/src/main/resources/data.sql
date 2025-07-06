-- Author 서비스 초기 데이터 스크립트
-- 데모 관리자 계정 생성

-- 기존 admin@kt.com 계정이 있는지 확인하고 없으면 삽입
INSERT INTO Author_table (
    id,
    email, 
    name, 
    introduction, 
    password,
    is_approved, 
    is_admin, 
    created_at, 
    updated_at
)
SELECT 
    1,
    'admin@kt.com',
    '시스템 관리자',
    'KT Library 시스템 관리자 계정입니다.',
    'admin123',
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM Author_table WHERE email = 'admin@kt.com'
);

-- 데모 작가 계정들 추가 (테스트용)
INSERT INTO Author_table (
    id,
    email, 
    name, 
    introduction, 
    password,
    is_approved, 
    is_admin, 
    created_at, 
    updated_at
)
SELECT 
    2,
    'author1@kt.com',
    '김작가',
    '작가1',
    'password',
    true,
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM Author_table WHERE email = 'author1@kt.com'
);

INSERT INTO Author_table (
    id,
    email,
    name,
    introduction,
    password,
    is_approved,
    is_admin,
    created_at,
    updated_at
)
SELECT
    3,
    'author2@kt.com',
    '이작가',
    '작가2',
    'password',
    false,
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM Author_table WHERE email = 'author2@kt.com'
);

-- 시퀀스 값 조정 (H2 데이터베이스용)
-- INSERT 후 다음 ID가 올바르게 생성되도록 시퀀스 값을 조정
ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 100;