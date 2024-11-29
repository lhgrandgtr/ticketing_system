package com.coursework.coursework.ticket;


import com.coursework.coursework.customer.CustomerService;
import com.coursework.coursework.vendor.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "api/ticket")
public class TicketController {
    private final TicketPool ticketPool;
    private final VendorService vendorService;
    private final CustomerService customerService;

    @Autowired
    public TicketController(
            TicketPool ticketPool,
            VendorService vendorService,
            CustomerService customerService) {
        this.ticketPool = ticketPool;
        this.vendorService = vendorService;
        this.customerService = customerService;
    }

    @GetMapping()
    public List<Ticket> getTickets() {
        return ticketPool.getAllTickets();
    }

    public record NewTicketRequest(
            String event,
            double price,
            LocalDate expireDate,
            double amount
    ) {}

    @PostMapping("/vendor/publish")
    public CompletableFuture<ResponseEntity<Ticket>> publishTicket(@RequestBody NewTicketRequest request) {
        return vendorService.publishTicket(request.event(), request.price(), request.expireDate(), request.amount())
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/vendor/{ticketId}")
    public CompletableFuture<ResponseEntity<Void>> removeTicket(@PathVariable("ticketId") Long id) {
        return vendorService.removeTicket(id)
                .thenApply(v -> ResponseEntity.ok().<Void>build())
                .exceptionally(e -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/vendor/{ticketId}")
    public CompletableFuture<ResponseEntity<Void>> updateTicket(
            @PathVariable("ticketId") Long ticketId,
            @RequestParam(required = false) String event,
            @RequestParam(required = false) LocalDate expireDate,
            @RequestParam(required = false) Double price
    ) {
        return vendorService.updateTicket(ticketId, event, expireDate, price)
                .thenApply(v -> ResponseEntity.ok().<Void>build())
                .exceptionally(e -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/customer/purchase")
    public CompletableFuture<ResponseEntity<Ticket>> purchaseTicket() {
        return customerService.purchaseTicket()
                .thenApply(ticketOpt -> ticketOpt
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/customer/search")
    public CompletableFuture<ResponseEntity<Ticket>> findTicketByEvent(@RequestParam String event) {
        return customerService.findTicketByEvent(event)
                .thenApply(ticketOpt -> ticketOpt
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/availability")
    public ResponseEntity<TicketAvailability> getTicketAvailability() {
        return ResponseEntity.ok(new TicketAvailability(
                ticketPool.hasAvailableTickets(),
                ticketPool.getCurrentCapacity()
        ));
    }

    public record TicketAvailability(boolean available, int currentCapacity) {}
}