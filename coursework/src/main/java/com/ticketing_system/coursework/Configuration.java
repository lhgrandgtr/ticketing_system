package com.ticketing_system.coursework;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ticket-sales")
public class Configuration {
    private int maxTickets;
    private double ticketReleaseRate;
    private double customerArrivalRate;
    private String saveFormat;

    public int getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }

    public double getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(double ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public double getCustomerArrivalRate() {
        return customerArrivalRate;
    }

    public void setCustomerArrivalRate(double customerArrivalRate) {
        this.customerArrivalRate = customerArrivalRate;
    }

    public String getSaveFormat() {
        return saveFormat;
    }

    public void setSaveFormat(String saveFormat) {
        this.saveFormat = saveFormat;
    }

    public void validateInput() {
        if (maxTickets <= 0) {
            throw new IllegalArgumentException("Max tickets must be positive");
        }
        if (ticketReleaseRate <= 0) {
            throw new IllegalArgumentException("Ticket release rate must be positive");
        }
        if (customerArrivalRate <= 0) {
            throw new IllegalArgumentException("Customer arrival rate must be positive");
        }
    }
}
