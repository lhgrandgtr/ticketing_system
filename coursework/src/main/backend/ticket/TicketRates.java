package main.backend.ticket;

public class TicketRates {
    private int ticketReleaseRate;
    private int customerRetrievalRate;

    public TicketRates(int ticketReleaseRate, int customerRetrievalRate) {
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
}