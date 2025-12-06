package com.next.relationship.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item_without_batch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemWithoutBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private int orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderWithoutBatch order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItemWithoutBatch(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.orderPrice = product.getPrice() * quantity;
    }

    public void setOrder(OrderWithoutBatch order) {
        this.order = order;
    }
}
