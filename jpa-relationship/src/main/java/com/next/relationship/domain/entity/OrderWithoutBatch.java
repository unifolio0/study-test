package com.next.relationship.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_without_batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderWithoutBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @OneToMany(mappedBy = "order")
    private List<OrderItemWithoutBatch> orderItems = new ArrayList<>();

    public OrderWithoutBatch(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void addOrderItem(OrderItemWithoutBatch orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
