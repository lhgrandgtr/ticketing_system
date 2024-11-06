package com.coursework.coursework.ticket;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Ticket {

    @Id
    @SequenceGenerator(
            name = "ticket_sequence",
            sequenceName = "ticket_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long id;

    private LocalDate createdAt;
    private LocalDate expireDate;
    private String event;
    private double price;

    public Ticket() {}

    public Ticket(Long id, LocalDate expireDate, String event, double price) {
        this.id = id;
        this.expireDate = expireDate;
        this.event = event;
        this.price = price;
    }

    public Ticket(LocalDate expireDate, String event, double price) {
        this.expireDate = expireDate;
        this.event = event;
        this.price = price;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", expireDate=" + expireDate +
                ", event='" + event + '\'' +
                ", price=" + price +
                '}';
    }
}
