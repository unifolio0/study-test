package com.next.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.config.QueryCountInspector;
import com.next.relationship.domain.entity.Order;
import com.next.relationship.domain.entity.OrderItem;
import com.next.relationship.domain.entity.Product;
import com.next.relationship.repository.OrderItemRepository;
import com.next.relationship.repository.OrderRepository;
import com.next.relationship.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BatchSizeAndSubselectTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void BatchSize로_N플러스1을_1플러스1로_완화() {
        Product productA = productRepository.save(new Product("상품A", 10000));
        Product productB = productRepository.save(new Product("상품B", 20000));

        for (int i = 1; i <= 5; i++) {
            Order order = new Order("ORDER-" + i);
            orderRepository.save(order);

            OrderItem item1 = new OrderItem(productA, 1);
            OrderItem item2 = new OrderItem(productB, 2);
            order.addOrderItem(item1);
            order.addOrderItem(item2);
            orderItemRepository.save(item1);
            orderItemRepository.save(item2);
        }

        entityManager.flush();
        entityManager.clear();

        QueryCountInspector.reset();

        List<Order> orders = orderRepository.findAll();
        int queryAfterFindAll = QueryCountInspector.getCount();
        System.out.println("findAll() 쿼리 수: " + queryAfterFindAll);

        int totalItems = 0;
        for (Order order : orders) {
            totalItems += order.getOrderItems().size();
        }

        int queryAfterAccess = QueryCountInspector.getCount();
        System.out.println("OrderItems 접근 후 쿼리 수: " + queryAfterAccess);

        assertThat(queryAfterFindAll).isEqualTo(1);
        assertThat(queryAfterAccess).isEqualTo(2);
    }
}
