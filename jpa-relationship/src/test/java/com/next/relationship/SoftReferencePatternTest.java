package com.next.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.config.QueryCountInspector;
import com.next.relationship.domain.entity.OrderItem;
import com.next.relationship.domain.entity.OrderItemSoftRef;
import com.next.relationship.domain.entity.Product;
import com.next.relationship.repository.OrderItemRepository;
import com.next.relationship.repository.OrderItemSoftRefRepository;
import com.next.relationship.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SoftReferencePatternTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemSoftRefRepository orderItemSoftRefRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void ManyToOne은_LAZY로딩시_N플러스1이_발생한다() {
        int itemCount = 10;
        for (int i = 1; i <= itemCount; i++) {
            Product product = productRepository.save(new Product("상품" + i, i * 1000));
            orderItemRepository.save(new OrderItem(product, 1));
        }

        entityManager.flush();
        entityManager.clear();

        QueryCountInspector.reset();

        List<OrderItem> items = orderItemRepository.findAll();
        int queryAfterFindAll = QueryCountInspector.getCount();

        for (OrderItem item : items) {
            item.getProduct().getName();
        }
        int queryAfterProductAccess = QueryCountInspector.getCount();

        System.out.println("OrderItem 조회: " + queryAfterFindAll + "쿼리");
        System.out.println("Product 접근 후: " + queryAfterProductAccess + "쿼리");
        System.out.println("N+1 발생: 1 + " + itemCount + " = " + (1 + itemCount));

        assertThat(queryAfterFindAll).isEqualTo(1);
        assertThat(queryAfterProductAccess).isEqualTo(1 + itemCount);
    }

    @Test
    void SoftReference는_배치조회로_N플러스1을_방지한다() {
        int itemCount = 10;
        for (int i = 1; i <= itemCount; i++) {
            Product product = productRepository.save(new Product("상품" + i, i * 1000));
            orderItemSoftRefRepository.save(new OrderItemSoftRef(product.getId(), 1, product.getPrice()));
        }

        entityManager.flush();
        entityManager.clear();

        QueryCountInspector.reset();

        List<OrderItemSoftRef> items = orderItemSoftRefRepository.findAll();
        int queryAfterFindAll = QueryCountInspector.getCount();

        List<Long> productIds = items.stream()
                .map(OrderItemSoftRef::getProductId)
                .toList();
        List<Product> products = productRepository.findAllById(productIds);
        int queryAfterBatchFetch = QueryCountInspector.getCount();

        System.out.println("OrderItemSoftRef 조회: " + queryAfterFindAll + "쿼리");
        System.out.println("Product 배치 조회 후: " + queryAfterBatchFetch + "쿼리");

        assertThat(queryAfterFindAll).isEqualTo(1);
        assertThat(queryAfterBatchFetch).isEqualTo(2);
    }

    @Test
    void ManyToOne은_실제_Product_객체가_필요하다() {
        Product product = productRepository.save(new Product("상품", 10000));

        OrderItem orderItem = new OrderItem(product, 1);
        orderItemRepository.save(orderItem);

        entityManager.flush();
        entityManager.clear();

        OrderItem found = orderItemRepository.findById(orderItem.getId()).orElseThrow();

        System.out.println("직접 탐색 가능: " + found.getProduct().getName());

        assertThat(found.getProduct()).isNotNull();
        assertThat(found.getProduct().getName()).isEqualTo("상품");
    }

    @Test
    void SoftReference는_존재하지않는_productId를_저장할수있다() {
        Long nonExistentProductId = 999999L;

        OrderItemSoftRef orderItem = new OrderItemSoftRef(nonExistentProductId, 1, 10000);
        orderItemSoftRefRepository.save(orderItem);

        entityManager.flush();
        entityManager.clear();

        OrderItemSoftRef found = orderItemSoftRefRepository.findById(orderItem.getId()).orElseThrow();
        List<Product> products = productRepository.findAllById(List.of(found.getProductId()));

        System.out.println("저장된 productId: " + found.getProductId());
        System.out.println("실제 Product 조회 결과: " + products.size() + "개");

        assertThat(found.getProductId()).isEqualTo(nonExistentProductId);
        assertThat(products).isEmpty();
    }

    @Test
    void SoftReference는_필요한_Product만_선택적으로_조회할수있다() {
        int itemCount = 10;
        for (int i = 1; i <= itemCount; i++) {
            Product product = productRepository.save(new Product("상품" + i, i * 1000));
            orderItemSoftRefRepository.save(new OrderItemSoftRef(product.getId(), 1, product.getPrice()));
        }

        entityManager.flush();
        entityManager.clear();

        QueryCountInspector.reset();

        List<OrderItemSoftRef> items = orderItemSoftRefRepository.findAll();

        List<OrderItemSoftRef> expensiveItems = items.stream()
                .filter(item -> item.getOrderPrice() >= 5000)
                .toList();

        List<Long> selectedProductIds = expensiveItems.stream()
                .map(OrderItemSoftRef::getProductId)
                .toList();

        List<Product> selectedProducts = productRepository.findAllById(selectedProductIds);
        int totalQueries = QueryCountInspector.getCount();

        Map<Long, Product> productMap = selectedProducts.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        System.out.println("전체 아이템: " + itemCount + "개");
        System.out.println("5000원 이상 아이템: " + expensiveItems.size() + "개");
        System.out.println("조회한 Product: " + selectedProducts.size() + "개");
        System.out.println("총 쿼리 수: " + totalQueries);

        assertThat(totalQueries).isEqualTo(2);
        assertThat(selectedProducts).hasSize(6);
        assertThat(productMap).hasSize(6);
    }
}
