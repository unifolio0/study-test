-- Member 테이블
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

-- Product 테이블
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price INT NOT NULL
);

-- Order 테이블 (orders로 명명 - order는 예약어)
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(255),
    member_id BIGINT,
    CONSTRAINT fk_orders_member FOREIGN KEY (member_id) REFERENCES member(id)
);

-- OrderItem 테이블
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    order_price INT NOT NULL,
    order_id BIGINT,
    product_id BIGINT,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- OrderItemSoftRef 테이블 (FK 없이 product_id를 일반 컬럼으로 저장)
CREATE TABLE order_item_soft_ref (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    quantity INT NOT NULL,
    order_price INT NOT NULL
);

-- OrderWithoutBatch 테이블
CREATE TABLE order_without_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(255)
);

-- OrderItemWithoutBatch 테이블
CREATE TABLE order_item_without_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    order_price INT NOT NULL,
    order_id BIGINT,
    product_id BIGINT,
    CONSTRAINT fk_order_item_without_batch_order FOREIGN KEY (order_id) REFERENCES order_without_batch(id),
    CONSTRAINT fk_order_item_without_batch_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- AvailableDate 테이블
CREATE TABLE available_date (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    available_date DATE
);

-- Reservation 테이블
CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    available_date_id BIGINT,
    capacity INT NOT NULL
);
