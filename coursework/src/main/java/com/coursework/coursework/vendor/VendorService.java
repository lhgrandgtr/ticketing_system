package com.coursework.coursework.vendor;

import com.coursework.coursework.ticket.Ticket;
import com.coursework.coursework.ticket.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
public class VendorService {
    private final TicketPool ticketPool;

    @Autowired
    public VendorService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Async
    public CompletableFuture<Ticket> publishTicket(String event, double price, LocalDate expireDate, double amount) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 / ticketPool.getTicketReleaseRate()); // Control release rate
                Ticket ticket = new Ticket();
                ticket.setEvent(event);
                ticket.setExpireDate(expireDate);
                ticket.setPrice(price);
                ticket.setAmount(amount);

                if (ticketPool.addTicket(ticket)) {
                    return ticket;
                }
                throw new IllegalStateException("Failed to publish ticket");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ticket publishing interrupted", e);
            }
        });
    }

    @Async
    public CompletableFuture<Void> removeTicket(Long ticketId) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 / ticketPool.getTicketReleaseRate());
                ticketPool.removeTicketById(ticketId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ticket removal interrupted", e);
            }
        });
    }

    @Async
    public CompletableFuture<Void> updateTicket(Long ticketId, String event, LocalDate expireDate, Double price) {
        return CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 / ticketPool.getTicketReleaseRate());
                ticketPool.updateTicket(ticketId, event, expireDate, price);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ticket update interrupted", e);
            }
        });
    }
}