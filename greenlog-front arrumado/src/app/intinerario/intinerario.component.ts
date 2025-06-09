import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Rota {
  nome: string;
  residuos: string[];
  distanciaKm: number;
}

interface Caminhao {
  placa: string;
  motorista: string;
  capacidadeKg: number;
  residuos: string[];
  indisponivel: boolean;
}

@Component({
  standalone: true,
  selector: 'app-intinerario',
  imports: [CommonModule, FormsModule],
  templateUrl: './intinerario.component.html',
  styleUrl: './intinerario.component.css'
})
export class IntinerarioComponent {
  selectedDate: Date | null = null;
  mesSelecionado: number = new Date().getMonth();
  anoSelecionado: number = new Date().getFullYear();

  formData = {
    rota: '',
    caminhao: ''
  };

  daysInMonth: Date[] = [];

  itinerariosSalvos: {
    data: Date;
    rota: Rota;
    caminhao: Caminhao;
  }[] = [];

  rotasDisponiveis: Rota[] = [
    { nome: 'Rota 1 - Norte', residuos: ['papel', 'plástico'], distanciaKm: 12 },
    { nome: 'Rota 2 - Sul', residuos: ['metal'], distanciaKm: 9 },
    { nome: 'Rota 3 - Leste', residuos: ['orgânico', 'papel'], distanciaKm: 15 }
  ];

  caminhoesDisponiveis: Caminhao[] = [
    { placa: 'ABC-1234', motorista: 'João', capacidadeKg: 1000, residuos: ['papel', 'plástico'], indisponivel: false },
    { placa: 'XYZ-5678', motorista: 'Maria', capacidadeKg: 1500, residuos: ['orgânico', 'papel'], indisponivel: false },
    { placa: 'JKL-0000', motorista: 'Carlos', capacidadeKg: 1200, residuos: ['metal'], indisponivel: false }
  ];

  ngOnInit() {
    this.generateCalendar();
  }

  generateCalendar() {
    const numDays = new Date(this.anoSelecionado, this.mesSelecionado + 1, 0).getDate();
    this.daysInMonth = [];

    for (let i = 1; i <= numDays; i++) {
      this.daysInMonth.push(new Date(this.anoSelecionado, this.mesSelecionado, i));
    }
  }

  mudarMes(delta: number) {
    this.mesSelecionado += delta;
    if (this.mesSelecionado > 11) {
      this.mesSelecionado = 0;
      this.anoSelecionado++;
    } else if (this.mesSelecionado < 0) {
      this.mesSelecionado = 11;
      this.anoSelecionado--;
    }
    this.generateCalendar();
  }

  openForm(date: Date) {
    this.selectedDate = date;
    this.formData = {
      rota: '',
      caminhao: ''
    };
  }

  saveItinerary() {
    if (!this.selectedDate) return;

    const rota = this.rotasDisponiveis.find(r => r.nome === this.formData.rota);
    const caminhao = this.caminhoesDisponiveis.find(c => c.placa === this.formData.caminhao);

    if (!rota || !caminhao) {
      alert('Seleção inválida.');
      return;
    }

    const residuosCompativeis = rota.residuos.every(res => caminhao.residuos.includes(res));
    if (!residuosCompativeis) {
      alert('Caminhão não compatível com todos os resíduos da rota.');
      return;
    }

    const conflito = this.itinerariosSalvos.some(it =>
      this.datasIguais(it.data, this.selectedDate!) &&
      it.caminhao.placa === caminhao.placa
    );

    if (conflito) {
      alert('Este caminhão já possui um itinerário neste dia.');
      return;
    }

    this.itinerariosSalvos.push({
      data: new Date(this.selectedDate),
      rota,
      caminhao
    });

    this.selectedDate = null;
  }

  removerItinerario(index: number) {
    this.itinerariosSalvos.splice(index, 1);
  }

  datasIguais(a: Date, b: Date): boolean {
    return a.getFullYear() === b.getFullYear() &&
           a.getMonth() === b.getMonth() &&
           a.getDate() === b.getDate();
  }

  cancel() {
    this.selectedDate = null;
  }

  getItinerariosDoDia(dia: Date) {
    return this.itinerariosSalvos.filter(it => this.datasIguais(it.data, dia));
  }

  formatarTooltip(dia: Date): string {
    return this.getItinerariosDoDia(dia)
      .map(it => `${it.rota.nome} - ${it.caminhao.placa}`)
      .join(', ');
  }
}
