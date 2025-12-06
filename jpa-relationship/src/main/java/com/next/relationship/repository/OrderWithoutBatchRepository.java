package com.next.relationship.repository;

import com.next.relationship.domain.entity.OrderWithoutBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderWithoutBatchRepository extends JpaRepository<OrderWithoutBatch, Long> {

}
