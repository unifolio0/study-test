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
class FetchJoinAndEntityGraphTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void Fetch_Join으로_N플러스1_해결() {
        Product productA = productRepository.save(new Product("상품A", 10000));
        Product productB = productRepository.save(new Product("상품B", 20000));

        for (int i = 1; i <= 3; i++) {
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

        List<Order> orders = orderRepository.findAllWithOrderItemsFetchJoin();
        int queryAfterFetchJoin = QueryCountInspector.getCount();
        System.out.println("Fetch Join 쿼리 수: " + queryAfterFetchJoin);

        int totalItems = 0;
        for (Order order : orders) {
            totalItems += order.getOrderItems().size();
        }

        int queryAfterAccess = QueryCountInspector.getCount();
        System.out.println("OrderItems 접근 후 쿼리 수: " + queryAfterAccess);
        System.out.println("Order 수: " + orders.size() + ", OrderItem 수: " + totalItems);

        assertThat(queryAfterFetchJoin).isEqualTo(1);
        assertThat(queryAfterAccess).isEqualTo(1);
    }

    @Test
    void EntityGraph로_N플러스1_해결() {
        Product productA = productRepository.save(new Product("상품A", 10000));
        Product productB = productRepository.save(new Product("상품B", 20000));

        for (int i = 1; i <= 3; i++) {
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

        List<Order> orders = orderRepository.findAllWithOrderItemsEntityGraph();
        int queryAfterEntityGraph = QueryCountInspector.getCount();
        System.out.println("EntityGraph 쿼리 수: " + queryAfterEntityGraph);

        int totalItems = 0;
        for (Order order : orders) {
            totalItems += order.getOrderItems().size();
        }

        int queryAfterAccess = QueryCountInspector.getCount();
        System.out.println("OrderItems 접근 후 쿼리 수: " + queryAfterAccess);
        System.out.println("Order 수: " + orders.size() + ", OrderItem 수: " + totalItems);

        assertThat(queryAfterEntityGraph).isEqualTo(1);
        assertThat(queryAfterAccess).isEqualTo(1);
    }

    @Test
    void Fetch_Join과_EntityGraph_차이_INNER_vs_LEFT_OUTER_JOIN() {
        Product productA = productRepository.save(new Product("상품A", 10000));

        Order orderWithItems = new Order("ORDER-WITH-ITEMS");
        orderRepository.save(orderWithItems);
        OrderItem item = new OrderItem(productA, 1);
        orderWithItems.addOrderItem(item);
        orderItemRepository.save(item);

        Order orderWithoutItems = new Order("ORDER-WITHOUT-ITEMS");
        orderRepository.save(orderWithoutItems);

        entityManager.flush();
        entityManager.clear();

        List<Order> fetchJoinResult = orderRepository.findAllWithOrderItemsFetchJoin();
        System.out.println("Fetch Join 결과 (INNER JOIN): " + fetchJoinResult.size() + "개");

        entityManager.clear();

        List<Order> entityGraphResult = orderRepository.findAllWithOrderItemsEntityGraph();
        System.out.println("EntityGraph 결과 (LEFT OUTER JOIN): " + entityGraphResult.size() + "개");

        assertThat(fetchJoinResult).hasSize(1);
        assertThat(entityGraphResult).hasSize(2);
    }
}
