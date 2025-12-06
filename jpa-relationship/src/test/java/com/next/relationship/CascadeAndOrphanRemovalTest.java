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
class CascadeAndOrphanRemovalTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void Cascade_PERSIST_부모_저장시_자식도_자동_저장된다() {
        Product product = productRepository.save(new Product("상품", 10000));

        Order order = new Order("ORDER-001");
        OrderItem orderItem1 = new OrderItem(product, 1);
        OrderItem orderItem2 = new OrderItem(product, 2);
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        long orderItemCount = orderItemRepository.count();
        System.out.println("Order 저장 후 OrderItem 개수: " + orderItemCount);

        assertThat(orderItemCount).isEqualTo(2);
    }

    @Test
    void Cascade_REMOVE_부모_삭제시_자식도_자동_삭제된다() {
        Product product = productRepository.save(new Product("상품", 10000));

        Order order = new Order("ORDER-001");
        OrderItem orderItem = new OrderItem(product, 1);
        order.addOrderItem(orderItem);
        orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        Order foundOrder = orderRepository.findById(order.getId()).orElseThrow();
        orderRepository.delete(foundOrder);

        entityManager.flush();
        entityManager.clear();

        long orderItemCount = orderItemRepository.count();
        System.out.println("Order 삭제 후 OrderItem 개수: " + orderItemCount);

        assertThat(orderItemCount).isZero();
    }

    @Test
    void orphanRemoval_컬렉션에서_제거하면_DB에서도_삭제된다() {
        Product product = productRepository.save(new Product("상품", 10000));

        Order order = new Order("ORDER-001");
        OrderItem orderItem1 = new OrderItem(product, 1);
        OrderItem orderItem2 = new OrderItem(product, 2);
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);
        orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        Order foundOrder = orderRepository.findById(order.getId()).orElseThrow();
        OrderItem itemToRemove = foundOrder.getOrderItems().get(0);
        foundOrder.removeOrderItem(itemToRemove);

        entityManager.flush();
        entityManager.clear();

        long orderItemCount = orderItemRepository.count();
        System.out.println("컬렉션에서 제거 후 OrderItem 개수: " + orderItemCount);

        assertThat(orderItemCount).isEqualTo(1);
    }
}
