import { Component, inject, OnInit, signal } from '@angular/core';
import { TicketService } from '../../services/ticket.service';
import { Ticket } from '../../model/ticket.type';
import { catchError } from 'rxjs';

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss'
}) 
export class TicketComponent implements OnInit {
  ticketService = inject(TicketService);
  tickets=  signal<Array<Ticket>>([]);
  ngOnInit(): void {
    this.ticketService.getAllTickets()
      .pipe(
        catchError((err: any) => {
          console.log("api error: " + err);
          throw err;
        })
      )
      .subscribe((ticket: Array<Ticket>) => {
        this.tickets.set(ticket);
      });
  }
}
