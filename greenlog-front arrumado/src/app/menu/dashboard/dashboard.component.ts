import { Component } from '@angular/core';
import { GraphComponent } from "../graph/graph.component";

@Component({
  selector: 'app-dashboard',
  imports: [GraphComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

}
