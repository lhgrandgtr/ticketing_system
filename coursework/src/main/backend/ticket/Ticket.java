package main.backend.ticket;

import java.time.LocalDate;

public class Ticket {

    private Long id;
    private final LocalDate createdAt;
    private LocalDate expireDate;
    private String event;
    private double price;
    private double amount;

    public Ticket() {
        this.createdAt = LocalDate.now();
    }

    public Ticket(LocalDate expireDate, String event, double price, double amount) {
        this.createdAt = LocalDate.now();
        this.expireDate = expireDate;
        this.event = event;
        this.price = price;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
                ", amount=" + amount +
                '}';
    }
}