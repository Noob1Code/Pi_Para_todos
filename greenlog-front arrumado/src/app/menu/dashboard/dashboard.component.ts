import { Component } from '@angular/core';
import { GraphComponent } from "../graph/graph.component";
import { GraficoLinhaComponent } from "../graficos/grafico-linha/grafico-linha.component";
import { GraficoPizzaComponent } from "../graficos/grafico-pizza/grafico-pizza.component";
import { NgxEchartsModule } from 'ngx-echarts';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgxEchartsModule,
    GraphComponent,
    GraficoLinhaComponent,
    GraficoPizzaComponent,
    FormsModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  anoSelecionado = 2025;
}