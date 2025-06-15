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

  private mesesDoAno = [ 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez' ];
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
        
        const series = [];
        const legendas = [];

        if (data && data.maiorDistanciaAnual) {
          const nomeSerie = `${data.maiorDistanciaAnual.placa} (Maior)`;
          legendas.push(nomeSerie);
          series.push({
            name: nomeSerie,
            type: 'line',
            smooth: true,
            data: data.maiorDistanciaAnual.distanciasMensais,
            lineStyle: { color: '#4CAF50' },
            itemStyle: { color: '#4CAF50' },
          });
        }

        if (data && data.menorDistanciaAnual) {
            const nomeSerie = `${data.menorDistanciaAnual.placa} (Menor)`;
            legendas.push(nomeSerie);
            series.push({
              name: nomeSerie,
              type: 'line',
              smooth: true,
              data: data.menorDistanciaAnual.distanciasMensais,
              lineStyle: { color: '#F44336' },
              itemStyle: { color: '#F44336' },
            });
        }
        
        this.chartOptions = {
          backgroundColor: '#12212F',
          title: {
            text: `Performance Mensal por Caminhão - ${ano}`,
            left: 'center',
            textStyle: { color: '#ffffff' },
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: legendas,
            textStyle: { color: '#ffffff' },
            bottom: 10,
          },
          grid: {
            left: '3%', right: '4%', bottom: '10%', containLabel: true
          },
          xAxis: [{
            type: 'category',
            boundaryGap: false,
            data: this.mesesDoAno,
            axisLine: { lineStyle: { color: '#ffffff' } },
            axisLabel: { color: '#ffffff' },
          }],
          yAxis: [{
            type: 'value',
            name: 'Distância (km)',
            axisLine: { lineStyle: { color: '#ffffff' } },
            axisLabel: { color: '#ffffff' },
            splitLine: { lineStyle: { color: '#2a3a4a' } }
          }],
          series: series,
        };
      });
  }

  onChartInit(ec: any) {
    this.echartsInstance = ec;
  }
}