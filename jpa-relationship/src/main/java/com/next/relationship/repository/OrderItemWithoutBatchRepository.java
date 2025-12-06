package com.next.relationship.repository;

import com.next.relationship.domain.entity.OrderItemWithoutBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemWithoutBatchRepository extends JpaRepository<OrderItemWithoutBatch, Long> {

}
