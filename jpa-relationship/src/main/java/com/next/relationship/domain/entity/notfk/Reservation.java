package com.next.relationship.domain.entity.notfk;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "available_date_id"
// 소프트 참조 연관관계 만드는 방법
// 1. ,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)   (ddl-auto를 create으로)
// 2, flyway로 소프트 참조로 먼저 스키마를 만든 후 ManyToOne 사용하기
    )
    private AvailableDate availableDate;

    private int capacity;

    public Reservation(AvailableDate availableDate, int capacity) {
        this.availableDate = availableDate;
        this.capacity = capacity;
    }
}
