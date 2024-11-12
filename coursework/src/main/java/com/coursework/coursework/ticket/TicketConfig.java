package com.coursework.coursework.ticket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class TicketConfig {
    @Bean
    CommandLineRunner commandLineRunner(
            TicketRepository repository) {
        return args -> {
            Ticket concert_ticket = new Ticket(
                    1L,
                    LocalDate.of(2024, Month.OCTOBER ,8),
                    "Taylor Shift concert",
                    2000,
                    2000
                    );
            Ticket orchestra_ticket = new Ticket(
                    2L,
                    LocalDate.of(2024, Month.MARCH, 16),
                    "Classical Orchestra play",
                    2000,
                    2500
            );
            repository.saveAll(
                    List.of(concert_ticket, orchestra_ticket)
            );
        };
    }
}
