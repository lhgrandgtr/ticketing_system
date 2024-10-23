package com.ticketing_system.coursework;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Vendor extends Person{
    @Id
    @SequenceGenerator(
            name = "ticket_id_sequence",
            sequenceName = "ticket_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_id_sequence"
    )

    private String vendorId;
    private TicketPool ticketPool;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public void setTicketPool(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(vendorId, vendor.vendorId) && Objects.equals(ticketPool, vendor.ticketPool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vendorId, ticketPool);
    }

    @Override
    public String toString() {
        return "Vendor{" +
                super.toString() +
                "vendorId='" + vendorId + '\'' +
                ", ticketPool=" + ticketPool +
                '}';
    }
}
