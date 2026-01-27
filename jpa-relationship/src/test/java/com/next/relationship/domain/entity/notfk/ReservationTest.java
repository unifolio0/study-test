package com.next.relationship.domain.entity.notfk;

import static org.assertj.core.api.Assertions.assertThat;

import com.next.relationship.repository.notfk.AvailableDateRepository;
import com.next.relationship.repository.notfk.ReservationRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@Transactional
class ReservationTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AvailableDateRepository availableDateRepository;
    @Autowired
    PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
    }

    @Test
    void test() {
        LocalDate now = LocalDate.now();
        Long id = transactionTemplate.execute(status -> {
            AvailableDate availableDate = availableDateRepository.save(new AvailableDate(now));
            Reservation reservation = reservationRepository.save(new Reservation(availableDate, 1));
            return reservation.getId();
        });
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        assertThat(reservation.getAvailableDate().getAvailableDate()).isEqualTo(now);
    }
}
