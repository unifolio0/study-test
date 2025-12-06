package com.next.relationship.repository;

import com.next.relationship.domain.entity.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems")
    List<Order> findAllWithOrderItemsFetchJoin();

    @EntityGraph(attributePaths = {"orderItems"})
    @Query("SELECT o FROM Order o")
    List<Order> findAllWithOrderItemsEntityGraph();

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems")
    Page<Order> findAllWithOrderItemsFetchJoinPaging(Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems")
    List<Order> findAllWithOrderItemsFetchJoinWithoutDistinct();
}
