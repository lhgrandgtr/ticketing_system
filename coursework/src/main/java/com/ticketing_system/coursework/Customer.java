package com.ticketing_system.coursework;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Customer extends Person{
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private Integer customerId;
    private List<Ticket> purchasedItems;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<Ticket> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<Ticket> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) && Objects.equals(purchasedItems, customer.purchasedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId, purchasedItems);
    }

    @Override
    public String toString() {
        return "Customer{" +
                super.toString() +
                ", customerId=" + customerId +
                ", PerchasedItems=" + purchasedItems +
                '}';
    }
}

