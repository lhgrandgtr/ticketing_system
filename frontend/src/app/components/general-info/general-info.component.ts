import { Component, inject, signal } from '@angular/core';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-general-info',
  standalone: true,
  imports: [],
  templateUrl: './general-info.component.html',
  styleUrl: './general-info.component.scss'
})
export class GeneralInfoComponent {
  totalTickets = signal(0); 
  availableTickets = signal(0); 
  releaseRate = signal(0);
  retrivalRate = signal(0); 

  private ticketService = inject(TicketService);

  constructor() {}

  ngOnInit() {
    this.loadTicketInfo();
  }

  loadTicketInfo() {
    this.ticketService.getTicketRates().subscribe((rates) => {
      this.releaseRate.set(rates.ticketReleaseRate); 
      this.retrivalRate.set(rates.customerRetrievalRate); 
    });
  }

  loadTicketCount() {
    this.ticketService.getTicketCount().subscribe((count) => {
      this.totalTickets.set(count);

    });
  }
}
