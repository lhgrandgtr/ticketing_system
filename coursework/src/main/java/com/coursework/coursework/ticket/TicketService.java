package com.coursework.coursework.ticket;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    public void publishTicket(Ticket ticket) {
        Optional<Ticket> ticketByEvent = ticketRepository.findTicketByEvent(ticket.getEvent());
        if (ticketByEvent.isPresent()) {
            throw new IllegalStateException("Event already exists.");
        }
        ticketRepository.save(ticket);
    }

    public void removeTicket(Long id) {

        if (!ticketRepository.existsById(id)) {
            throw new IllegalStateException(
                    "Ticket with id " + id + "does not exist."
            );
        }
        ticketRepository.deleteById(id);
    }

    @Transactional
    public void updateTicket(Long ticketId, String event, LocalDate expireDate, Double price) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalStateException("Ticket with id " + ticketId + " does not exist."));

        if (event != null && !event.isEmpty() && !Objects.equals(ticket.getEvent(), event)) {
            Optional<Ticket> ticketOptional = ticketRepository.findTicketByEvent(event);
            if (ticketOptional.isPresent()) {
                throw new IllegalStateException("Event already exists.");
            }
            ticket.setEvent(event);
        }

        if (expireDate != null && !Objects.equals(ticket.getExpireDate(), expireDate)) {
            ticket.setExpireDate(expireDate);
        }

        if (price != null) {
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive.");
            }
            if (!Objects.equals(ticket.getPrice(), price)) {
                ticket.setPrice(price);
            }
        }
    }

}