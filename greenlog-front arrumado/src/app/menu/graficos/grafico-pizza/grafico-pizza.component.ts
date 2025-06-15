import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgxEchartsModule } from 'ngx-echarts';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-grafico-pizza',
  standalone: true,
  imports: [NgxEchartsModule, CommonModule],
  templateUrl: './grafico-pizza.component.html',
  styleUrls: ['./grafico-pizza.component.css'],
})
export class GraficoPizzaComponent implements OnInit {
  chartOptions: any;
  private echartsInstance: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8080/api/relatorios/itinerarios-por-dia')
      .subscribe(data => {
        const seriesData = data.map(d => ({
          name: d.diaDaSemana,
          value: d.quantidade
        }));

        this.chartOptions = {
          backgroundColor: '#12212F',
          title: {
            text: 'Itinerários por Dia da Semana',
            left: 'center',
            textStyle: { color: '#ffffff' },
          },
          tooltip: {
            trigger: 'item',
            backgroundColor: '#333',
            textStyle: { color: '#ffffff' },
          },
          legend: {
            bottom: '0%',
            left: 'center',
            textStyle: { color: '#ffffff' },
          },
          series: [
            {
              name: 'Itinerários',
              type: 'pie',
              radius: '50%',
              data: seriesData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: { color: '#ffffff' },
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