import { Routes } from '@angular/router';

export const routes: Routes = [{
    path: '',
    pathMatch: 'full',
    loadComponent: () => {
        return import('./home/home.component').then((m) => m.HomeComponent);
    },
  },
  {
    path: 'ticket',
    loadComponent: () => {
        return import('./components/ticket/ticket.component').then((m) => m.TicketComponent);
    },
  }

];