package com.next.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.domain.entity.Order;
import com.next.relationship.domain.entity.OrderItem;
import com.next.relationship.domain.entity.Product;
import com.next.relationship.repository.OrderItemRepository;
import com.next.relationship.repository.OrderRepository;
import com.next.relationship.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BidirectionalMappingTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 연관관계_주인쪽만_설정하면_영속성_컨텍스트에서_양방향_탐색이_안된다() {
        Product product = productRepository.save(new Product("상품", 10000));
        Order order = orderRepository.save(new Order("ORDER-001"));

        OrderItem orderItem = new OrderItem(product, 1);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

        System.out.println("OrderItem에서 Order 조회: " + orderItem.getOrder().getOrderNumber());
        System.out.println("Order에서 OrderItems 개수: " + order.getOrderItems().size());

        assertThat(orderItem.getOrder()).isEqualTo(order);
        assertThat(order.getOrderItems()).isEmpty();
    }

    @Test
    void 편의_메서드를_사용하면_양방향_동기화가_보장된다() {
        Product product = productRepository.save(new Product("상품", 10000));
        Order order = orderRepository.save(new Order("ORDER-001"));

        OrderItem orderItem = new OrderItem(product, 1);
        order.addOrderItem(orderItem);
        orderItemRepository.save(orderItem);

        System.out.println("OrderItem에서 Order 조회: " + orderItem.getOrder().getOrderNumber());
        System.out.println("Order에서 OrderItems 개수: " + order.getOrderItems().size());

        assertThat(orderItem.getOrder()).isEqualTo(order);
        assertThat(order.getOrderItems()).contains(orderItem);
    }

    @Test
    void 영속성_컨텍스트_초기화_후에는_DB에서_양방향_탐색이_가능하다() {
        Product product = productRepository.save(new Product("상품", 10000));
        Order order = orderRepository.save(new Order("ORDER-001"));

        OrderItem orderItem = new OrderItem(product, 1);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);

        entityManager.flush();
        entityManager.clear();

        Order foundOrder = orderRepository.findById(order.getId()).orElseThrow();

        System.out.println("DB 조회 후 Order에서 OrderItems 개수: " + foundOrder.getOrderItems().size());

        assertThat(foundOrder.getOrderItems()).hasSize(1);
    }
}
