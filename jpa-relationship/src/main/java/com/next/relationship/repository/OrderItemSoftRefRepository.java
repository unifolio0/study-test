package com.next.relationship.repository;

import com.next.relationship.domain.entity.OrderItemSoftRef;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemSoftRefRepository extends JpaRepository<OrderItemSoftRef, Long> {

}
