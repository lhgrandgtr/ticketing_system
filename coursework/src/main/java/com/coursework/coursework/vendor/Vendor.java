package com.coursework.coursework.vendor;


import com.coursework.coursework.Logger;
import com.coursework.coursework.ticket.Ticket;
import com.coursework.coursework.ticket.TicketPool;
import java.time.LocalDate;
import java.util.Random;

public class Vendor implements Runnable {
    private final int vendorId;
    private final TicketPool ticketPool;
    private final Random random;
    private final Logger logger;

    public Vendor(int vendorId, TicketPool ticketPool, Logger logger) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.random = new Random();
        this.logger = logger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = createRandomTicket();
                ticketPool.addTicket(ticket);
                logger.log(String.format("Vendor %d added ticket for event: %s", vendorId, ticket.getEvent()));

                // Random delay between adding tickets
                Thread.sleep(500 + random.nextInt(1500));
            } catch (IllegalStateException e) {
                logger.log(String.format("Vendor %d: %s", vendorId, e.getMessage()));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Ticket createRandomTicket() {
        String[] eventTypes = {"Concert", "Sports", "Theater", "Festival", "Comedy"};
        String event = String.format("%s-V%d-%d",
                eventTypes[random.nextInt(eventTypes.length)],
                vendorId,
                random.nextInt(1000));

        LocalDate expireDate = LocalDate.now().plusDays(random.nextInt(30) + 1);
        double price = 50 + random.nextDouble() * 150;
        double amount = 1 + random.nextInt(5);

        return new Ticket(expireDate, event, price, amount);
    }
}