package com.example.springbatchtutorial.domain.order;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private Integer price;
    private ZonedDateTime orderDate;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderItem='" + orderItem + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                '}';
    }
}
