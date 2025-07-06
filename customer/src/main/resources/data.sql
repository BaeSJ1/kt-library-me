-- Customer 테스트 데이터
INSERT INTO customer_table (id, name, email, password, is_kt_user, created_at, updated_at) 
VALUES (1, '고객', 'customer@kt.com', 'password', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO read_book_table (id,book_id, book_shelf_id, title, price) 
VALUES (1,3, 101, 'Sample Book Title', 15000);


