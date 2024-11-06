package com.coursework.coursework.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/ticket")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping()
    public List<Ticket> getTickets() {
        return ticketService.getTickets();

    }

    public record NewTicketRequest(
            String event,
            double price,
            LocalDate expireDate,
            LocalDate cratedAt
    ) {}

    @PostMapping()
    public void publishTicket(@RequestBody NewTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setEvent(request.event);
        ticket.setExpireDate(request.expireDate);
        ticket.setPrice(request.price);
        ticketService.publishTicket(ticket);
    }

    @DeleteMapping("{ticketId}")
    public void removeTicket(@PathVariable("ticketId") Long id){
        ticketService.removeTicket(id);
    }

    @PutMapping(path = "{ticketId}")
    public void updateTicket(
            @PathVariable("ticketId") Long ticketId,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) LocalDate expireDate,
            @RequestParam(required = false) double price
    ) {
        ticketService.updateTicket(ticketId, event, expireDate, price);
    }

}
