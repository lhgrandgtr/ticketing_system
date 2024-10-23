package com.ticketing_system.coursework;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class TicketPool {
    private ConcurrentLinkedQueue<Ticket> tickets = new ConcurrentLinkedQueue<>();

    public synchronized void addTickets(int quantity) {
        for (int i = 0; i < quantity; i++) {
            tickets.offer(new Ticket("Ticket-" + System.currentTimeMillis(), 10.0));
        }
    }

    public synchronized Ticket removeTicket() {
        return tickets.poll();
    }

    public int getAvailableTickets() {
        return tickets.size();
    }
}
