package com.next.relationship.repository.notfk;

import com.next.relationship.domain.entity.notfk.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
