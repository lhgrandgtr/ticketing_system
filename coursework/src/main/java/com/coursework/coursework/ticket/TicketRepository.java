package com.coursework.coursework.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

//    @Query("SELECT s FROM ticket t WHERE t.event = ?1")
    Optional<Ticket> findTicketByEvent(String event);
}