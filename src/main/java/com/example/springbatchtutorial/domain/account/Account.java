package com.example.springbatchtutorial.domain.account;

import com.example.springbatchtutorial.domain.order.Order;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderItem;
    private Integer price;
    private ZonedDateTime orderDate;
    private ZonedDateTime accountDate;

    public Account(final Order order) {
        this.id = order.getId();
        this.orderItem = order.getOrderItem();
        this.price = order.getPrice();
        this.orderDate = order.getOrderDate();
        this.accountDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", orderItem='" + orderItem + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                ", accountDate=" + accountDate +
                '}';
    }
}
