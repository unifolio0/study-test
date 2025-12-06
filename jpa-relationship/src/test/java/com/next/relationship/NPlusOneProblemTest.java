package com.next.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.config.QueryCountInspector;
import com.next.relationship.domain.entity.OrderItemWithoutBatch;
import com.next.relationship.domain.entity.OrderWithoutBatch;
import com.next.relationship.domain.entity.Product;
import com.next.relationship.repository.OrderItemWithoutBatchRepository;
import com.next.relationship.repository.OrderWithoutBatchRepository;
import com.next.relationship.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NPlusOneProblemTest {

    @Autowired
    private OrderWithoutBatchRepository orderRepository;

    @Autowired
    private OrderItemWithoutBatchRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void Order_조회_후_OrderItems_접근시_N플러스1_문제_발생() {
        Product productA = productRepository.save(new Product("상품A", 10000));
        Product productB = productRepository.save(new Product("상품B", 20000));

        int orderCount = 3;
        for (int i = 1; i <= orderCount; i++) {
            OrderWithoutBatch order = new OrderWithoutBatch("ORDER-" + i);
            orderRepository.save(order);

            OrderItemWithoutBatch item1 = new OrderItemWithoutBatch(productA, 1);
            OrderItemWithoutBatch item2 = new OrderItemWithoutBatch(productB, 2);
            order.addOrderItem(item1);
            order.addOrderItem(item2);
            orderItemRepository.save(item1);
            orderItemRepository.save(item2);
        }

        entityManager.flush();
        entityManager.clear();

        QueryCountInspector.reset();

        List<OrderWithoutBatch> orders = orderRepository.findAll();
        int queryAfterFindAll = QueryCountInspector.getCount();

        for (OrderWithoutBatch order : orders) {
            order.getOrderItems().size();
        }

        int queryAfterAccess = QueryCountInspector.getCount();

        System.out.println("Order 개수: " + orderCount);
        System.out.println("findAll() 쿼리 수: " + queryAfterFindAll);
        System.out.println("OrderItems 접근 후 총 쿼리 수: " + queryAfterAccess);
        System.out.println("N+1 발생: 1 + " + orderCount + " = " + (1 + orderCount));

        assertThat(queryAfterFindAll).isEqualTo(1);
        assertThat(queryAfterAccess).isEqualTo(1 + orderCount);
    }
}
