package main.backend.ticket;

import main.backend.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TicketPool {
    private final ConcurrentLinkedQueue<Ticket> ticketQueue;
    private final AtomicInteger currentCapacity;
    private final int maxTicketCapacity;
    private final int ticketReleaseRate;
    private final int customerRetrievalRate;
    private final AtomicLong idGenerator = new AtomicLong(1); // AtomicLong for unique ID generation
    private final Logger logger;

    public TicketPool(int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, Logger logger) {
        this.ticketQueue = new ConcurrentLinkedQueue<>();
        this.currentCapacity = new AtomicInteger(0);
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.logger = logger;
        logger.log("TicketPool initialized with max capacity: " + maxTicketCapacity);
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public Optional<Ticket> removeTicket() {
        Ticket ticket = ticketQueue.poll();
        if (ticket != null) {
            currentCapacity.decrementAndGet();
            logger.log("Removed ticket: " + ticket);
            return Optional.of(ticket);
        }
        logger.log("No tickets available to remove.");
        return Optional.empty();
    }

    public List<Ticket> getAllTickets() {
        logger.log("Retrieved all tickets.");
        return new ArrayList<>(ticketQueue);
    }

    public int getCurrentCapacity() {
        return currentCapacity.get();
    }

    public boolean hasAvailableTickets() {
        boolean available = !ticketQueue.isEmpty();
        logger.log("Checked ticket availability: " + available);
        return available;
    }

    public Optional<Ticket> findTicketByEvent(String event) {
        Optional<Ticket> ticket = ticketQueue.stream()
                .filter(t -> t.getEvent().equals(event))
                .findFirst();
        logger.log("Searched for ticket by event: " + event + ", Found: " + ticket.isPresent());
        return ticket;
    }

    public boolean addTicket(Ticket ticket) {
        if (findTicketByEvent(ticket.getEvent()).isPresent()) {
            logger.log("Failed to add ticket. Event already exists: " + ticket.getEvent());
            throw new IllegalStateException("Event already exists");
        }
        if (currentCapacity.get() >= maxTicketCapacity) {
            logger.log("Failed to add ticket. Pool has reached maximum capacity.");
            throw new IllegalStateException("Ticket pool has reached maximum capacity");
        }

        ticket.setId(idGenerator.getAndIncrement()); // Set unique ID for the ticket
        boolean added = ticketQueue.offer(ticket);
        if (added) {
            currentCapacity.incrementAndGet();
            logger.log("Added ticket: " + ticket);
        } else {
            logger.log("Failed to add ticket: " + ticket);
        }
        return added;
    }

    public Optional<Ticket> purchaseTicket() {
        Optional<Ticket> ticket = removeTicket();
        if (ticket.isPresent()) {
            logger.log("Ticket purchased: " + ticket.get());
        } else {
            logger.log("Failed to purchase ticket. No tickets available.");
        }
        return ticket;
    }
}
