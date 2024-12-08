package main.coursework.ticket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketPool ticketPool;

    public TicketController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    // Get current ticket count
    @GetMapping("/count")
    public ResponseEntity<Integer> getTicketCount() {
        return ResponseEntity.ok(ticketPool.getCurrentCapacity());
    }

    // Get max ticket capacity
    @GetMapping("/capacity")
    public ResponseEntity<Integer> getMaxTicketCapacity() {
        return ResponseEntity.ok(ticketPool.getMaxTicketCapacity());
    }

    // Check if tickets are available
    @GetMapping("/available")
    public ResponseEntity<Boolean> hasAvailableTickets() {
        return ResponseEntity.ok(ticketPool.hasAvailableTickets());
    }

    // Get all tickets
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketPool.getAllTickets());
    }

    // Find ticket by event
    @GetMapping("/event/{eventName}")
    public ResponseEntity<Ticket> findTicketByEvent(@PathVariable String eventName) {
        Optional<Ticket> ticket = ticketPool.findTicketByEvent(eventName);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Find ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> findTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketPool.findTicketById(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new ticket
    @PostMapping
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket) {
        try {
            ticketPool.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Purchase a ticket
    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket() {
        Optional<Ticket> ticket = ticketPool.purchaseTicket();
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    // Update a ticket
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable Long id,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) LocalDate expireDate,
            @RequestParam(required = false) Double price) {
        try {
            ticketPool.updateTicket(id, event, expireDate, price);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Remove a ticket by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTicketById(@PathVariable Long id) {
        try {
            ticketPool.removeTicketById(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get ticket release and retrieval rates
    @GetMapping("/rates")
    public ResponseEntity<TicketRates> getTicketRates() {
        return ResponseEntity.ok(new TicketRates(
                ticketPool.getTicketReleaseRate(),
                ticketPool.getCustomerRetrievalRate()
        ));
    }
}