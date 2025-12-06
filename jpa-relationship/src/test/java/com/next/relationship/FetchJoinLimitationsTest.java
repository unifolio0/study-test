package com.next.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.domain.entity.Order;
import com.next.relationship.domain.entity.OrderItem;
import com.next.relationship.domain.entity.Product;
import com.next.relationship.repository.OrderItemRepository;
import com.next.relationship.repository.OrderRepository;
import com.next.relationship.repository.ProductRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FetchJoinLimitationsTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void Fetch_Join과_페이징을_함께_사용하면_경고가_발생한다() {
        Logger hibernateLogger = (Logger) LoggerFactory.getLogger("org.hibernate.orm.query");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        hibernateLogger.addAppender(listAppender);
        hibernateLogger.setLevel(Level.WARN);

        Product product = productRepository.save(new Product("상품", 10000));

        for (int i = 1; i <= 10; i++) {
            Order order = new Order("ORDER-" + i);
            orderRepository.save(order);

            OrderItem item = new OrderItem(product, i);
            order.addOrderItem(item);
            orderItemRepository.save(item);
        }

        entityManager.flush();
        entityManager.clear();

        int pageSize = 3;
        Page<Order> page = orderRepository.findAllWithOrderItemsFetchJoinPaging(PageRequest.of(0, pageSize));

        List<ILoggingEvent> logsList = listAppender.list;
        boolean hasWarning = logsList.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("firstResult/maxResults specified with collection fetch"));

        System.out.println("요청한 페이지 크기: " + pageSize);
        System.out.println("실제 반환된 크기: " + page.getContent().size());
        System.out.println("Hibernate 경고 발생: " + hasWarning);
        if (hasWarning) {
            System.out.println("경고 내용: firstResult/maxResults specified with collection fetch; applying in memory");
        }

        assertThat(page.getContent()).hasSize(pageSize);
        assertThat(hasWarning).isTrue();

        hibernateLogger.detachAppender(listAppender);
    }

    /**
     * @see <a href="https://docs.hibernate.org/orm/current/userguide/html_single/#hql-distinct">Hibernate DISTINCT</a>
     */
    @Test
    void Hibernate6부터_FetchJoin은_자동으로_중복제거한다() {
        Product product = productRepository.save(new Product("상품", 10000));

        int orderCount = 2;
        int itemsPerOrder = 3;

        for (int i = 1; i <= orderCount; i++) {
            Order order = new Order("ORDER-" + i);
            orderRepository.save(order);

            for (int j = 1; j <= itemsPerOrder; j++) {
                OrderItem item = new OrderItem(product, j);
                order.addOrderItem(item);
                orderItemRepository.save(item);
            }
        }

        entityManager.flush();
        entityManager.clear();

        List<Order> ordersWithoutDistinct = orderRepository.findAllWithOrderItemsFetchJoinWithoutDistinct();
        List<Order> ordersWithDistinct = orderRepository.findAllWithOrderItemsFetchJoin();

        System.out.println("Order 개수: " + orderCount);
        System.out.println("Order당 OrderItem 개수: " + itemsPerOrder);
        System.out.println("DISTINCT 없이 조회 결과: " + ordersWithoutDistinct.size() + "개");
        System.out.println("DISTINCT 있이 조회 결과: " + ordersWithDistinct.size() + "개");

        assertThat(ordersWithoutDistinct).hasSize(orderCount);
        assertThat(ordersWithDistinct).hasSize(orderCount);
    }
}
