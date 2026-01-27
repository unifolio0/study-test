package com.next.relationship.repository.notfk;

import com.next.relationship.domain.entity.notfk.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableDateRepository extends JpaRepository<AvailableDate, Long> {
}
