package com.coursework.coursework.customer;

import com.coursework.coursework.ticket.Ticket;
import com.coursework.coursework.ticket.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CustomerService {
    private final TicketPool ticketPool;

    @Autowired
    public CustomerService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Async
    public CompletableFuture<Optional<Ticket>> purchaseTicket() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 / ticketPool.getCustomerRetrievalRate()); // Control customer purchase rate
                return ticketPool.purchaseTicket();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ticket purchase interrupted", e);
            }
        });
    }

    @Async
    public CompletableFuture<Optional<Ticket>> findTicketByEvent(String event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100); // Small delay to prevent overwhelming the system
                return ticketPool.findTicketByEvent(event);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Ticket search interrupted", e);
            }
        });
    }
}