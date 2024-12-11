package main.backend.ticket;
import main.backend.ticket.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    public record NewTicketRequest(
            String event,
            double price,
            LocalDate createdAt,
            LocalDate expireDate,
            double amount
    ) {}

    private final TicketPool ticketPool;

    public TicketController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getTicketCount() {
        return ResponseEntity.ok(ticketPool.getCurrentCapacity());
    }

    @GetMapping("/capacity")
    public ResponseEntity<Integer> getMaxTicketCapacity() {
        return ResponseEntity.ok(ticketPool.getMaxTicketCapacity());
    }

    @GetMapping("/get-all-tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketPool.getAllTickets());
    }

    @PostMapping("/add-ticket")
    public ResponseEntity<Ticket> addTicket(@RequestBody NewTicketRequest request) {
        try {
            Ticket ticket = new Ticket(request.expireDate(), request.event(), request.price(), request.amount());
            ticketPool.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<TicketRates> getTicketRates() {
        return ResponseEntity.ok(new TicketRates(
                ticketPool.getTicketReleaseRate(),
                ticketPool.getCustomerRetrievalRate()
        ));
    }

    @GetMapping("/events")
    public ResponseEntity<List<String>> getAllEventNames() {
        List<String> events = ticketPool.getAllTickets()
                .stream()
                .map(Ticket::getEvent)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(events);
    }

}