import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NgxEchartsModule } from 'ngx-echarts';

@Component({
  selector: 'app-grafico-pizza',
  standalone: true,
  imports: [NgxEchartsModule, CommonModule],
  templateUrl: './grafico-pizza.component.html',
  styleUrls: ['./grafico-pizza.component.css'],  // Corrigido para styleUrls
})
export class GraficoPizzaComponent {
  chartOptions = {
  backgroundColor: '#12212F',  // Fundo do gráfico

  title: {
    text: 'Distribuição de Produtos',
    left: 'center',
    textStyle: {
      color: '#ffffff',         // Cor do título
    },
  },

  tooltip: {
    trigger: 'item',
    backgroundColor: '#333',     // Fundo do tooltip (opcional)
    textStyle: {
      color: '#ffffff',          // Texto branco no tooltip
    },
  },

  legend: {
    bottom: '0%',
    left: 'center',
    textStyle: {
      color: '#ffffff',          // Cor da legenda
    },
  },

  series: [
    {
      name: 'Produtos',
      type: 'pie',
      radius: '50%',
      data: [
        { value: 1048, name: 'Eletrônicos' },
        { value: 735, name: 'Roupas' },
        { value: 580, name: 'Alimentos' },
        { value: 484, name: 'Livros' },
        { value: 300, name: 'Outros' },
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)',
        },
      },
      label: {
        color: '#ffffff',          // Cor dos textos nas fatias
      },
    },
  ],
};

  private echartsInstance: any;

  onChartInit(ec: any) {
    this.echartsInstance = ec;
    // Garante que o gráfico seja redimensionado após a criação para evitar erro de tamanho 0
    setTimeout(() => this.echartsInstance.resize(), 0);
  }
}
