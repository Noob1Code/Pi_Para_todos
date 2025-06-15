import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NgxEchartsModule } from 'ngx-echarts';

@Component({
  selector: 'app-grafico-linha',
  standalone: true,
  imports: [CommonModule, NgxEchartsModule],
  templateUrl: './grafico-linha.component.html',
  styleUrls: ['./grafico-linha.component.css'],
})
export class GraficoLinhaComponent implements OnChanges {
  @Input() ano!: number;
  chartOptions: any;
  private echartsInstance: any;

  constructor(private http: HttpClient) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['ano'] && this.ano) {
      this.carregarDados(this.ano);
    }
  }

  carregarDados(ano: number) {
    this.http.get<any>(`http://localhost:8080/api/relatorios/extremos-distancia?ano=${ano}`)
      .subscribe(data => {
        const nomes = [
          `${data.maiorDistancia.caminhao.placa} (Maior)`,
          `${data.menorDistancia.caminhao.placa} (Menor)`
        ];
        const valores = [
          data.maiorDistancia.quilometrosPercorridos,
          data.menorDistancia.quilometrosPercorridos
        ];

        this.chartOptions = {
          backgroundColor: '#12212F',
          title: {
            text: `Distância Percorrida - ${ano}`,
            textStyle: { color: '#ffffff' },
          },
          tooltip: {},
          xAxis: {
            data: nomes,
            axisLine: { lineStyle: { color: '#ffffff' } },
            axisLabel: { color: '#ffffff' },
          },
          yAxis: {
            axisLine: { lineStyle: { color: '#ffffff' } },
            axisLabel: { color: '#ffffff' },
          },
          series: [
            {
              name: 'Distância (km)',
              type: 'line',
              data: valores,
              lineStyle: { color: '#4CAF50' },
              itemStyle: { color: '#4CAF50' },
            },
          ],
        };

        setTimeout(() => this.echartsInstance?.resize(), 0);
      });
  }

  onChartInit(ec: any) {
    this.echartsInstance = ec;
  }
}
