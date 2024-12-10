import { Component } from '@angular/core';
import { HeaderComponent } from "../components/header/header.component";
import { TicketComponent } from "../components/ticket/ticket.component";
import { GeneralInfoComponent } from '../components/general-info/general-info.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [GeneralInfoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
