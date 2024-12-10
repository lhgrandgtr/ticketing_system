package main.backend.customer;

import main.backend.Logger;
import main.backend.ticket.TicketPool;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private final int customerId;
    private final TicketPool ticketPool;
    private final Random random;
    private final Logger logger;
    private final AtomicInteger successfulPurchases;
    private final AtomicInteger failedPurchases;

    public Customer(int customerId, TicketPool ticketPool, Logger logger,
                    AtomicInteger successfulPurchases, AtomicInteger failedPurchases) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.random = new Random();
        this.logger = logger;
        this.successfulPurchases = successfulPurchases;
        this.failedPurchases = failedPurchases;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                var purchasedTicket = ticketPool.purchaseTicket();
                if (purchasedTicket.isPresent()) {
                    successfulPurchases.incrementAndGet();
                    logger.log(String.format("Customer %d purchased ticket for event: %s",
                            customerId, purchasedTicket.get().getEvent()));
                } else {
                    failedPurchases.incrementAndGet();
                    logger.log(String.format("Customer %d: No tickets available", customerId));
                }

                // Random delay between purchase attempts
                Thread.sleep(200 + random.nextInt(800));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}