import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgxEchartsModule } from 'ngx-echarts';

@Component({
  selector: 'app-grafico-linha',
  standalone: true,
  imports: [CommonModule, NgxEchartsModule],
  templateUrl: './grafico-linha.component.html',
  styleUrls: ['./grafico-linha.component.css'],  // corrigido
})
export class GraficoLinhaComponent {
  chartOptions = {
  backgroundColor: '#12212F',  // <- Aqui
  title: {
    text: 'Vendas por Mês',
    textStyle: {
      color: '#ffffff',        // Cor do título para contrastar
    },
  },
  tooltip: {},
  xAxis: {
    data: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
    axisLine: { lineStyle: { color: '#ffffff' } },  // Cor dos eixos
    axisLabel: { color: '#ffffff' },               // Cor dos textos no eixo
  },
  yAxis: {
    axisLine: { lineStyle: { color: '#ffffff' } },
    axisLabel: { color: '#ffffff' },
  },
  series: [
    {
      name: 'Vendas',
      type: 'line',
      data: [500, 800, 600, 1200, 900, 1100],
      lineStyle: {
        color: '#4CAF50',  // Cor da linha (verde por exemplo)
      },
      itemStyle: {
        color: '#4CAF50',  // Cor dos pontos
      },
    },
  ],
};

  private echartsInstance: any;

  onChartInit(ec: any) {
    this.echartsInstance = ec;
    // Força o resize para evitar problema de width/height 0
    setTimeout(() => this.echartsInstance.resize(), 0);
  }
}