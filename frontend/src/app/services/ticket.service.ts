
import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Ticket } from '../model/ticket.type';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private baseUrl = 'http://localhost:8080/api/tickets'; 
  Http = inject(HttpClient);

   getAllTickets() {
    return this.Http.get<Array<Ticket>>(`${this.baseUrl}/get-all-tickets`);
  }

  getTicketRates() {
    return this.Http.get<{ ticketReleaseRate: number; customerRetrievalRate: number }>(
      `${this.baseUrl}/rates`
    );
  }

  getTicketCount() {
    return this.Http.get<number>(`${this.baseUrl}/count`);
  }

  hasAvailableTickets() {
    return this.Http.get<boolean>(`${this.baseUrl}/available`);
  }
}

