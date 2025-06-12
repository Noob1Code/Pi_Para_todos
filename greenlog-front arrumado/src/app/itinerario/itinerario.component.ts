// Atualizado: ItinerarioComponent com backend + edição/exclusão
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalRotaComponent } from "../padronizador/modal/modal-rota/modal-rota.component";
import { Rota } from '../cadastrar/rota/rota.model';
import { Caminhao } from '../cadastrar/caminhao/caminhao.modal';
import { ModalCaminhaoComponent } from "../padronizador/modal/modal-caminhao/modal-caminhao.component";
import { ItinerarioService } from './itinerario.service';
import { Itinerario } from './itinerario.model';

@Component({
  standalone: true,
  selector: 'app-itinerario',
  imports: [CommonModule, FormsModule, ModalRotaComponent, ModalCaminhaoComponent],
  templateUrl: './itinerario.component.html',
  styleUrl: './itinerario.component.css'
})
export class ItinerarioComponent implements OnInit {
  diasDaSemana: string[] = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
  mesSelecionado: number = new Date().getMonth();
  anoSelecionado: number = new Date().getFullYear();

  diasDoMes: Date[] = [];
  diasVazios: any[] = [];
  diaConsultado: Date | null = null;
  diaSelecionado: Date | null = null;

  modalRotasVisivel = false;
  modalCaminhaoVisivel = false;

  rotaSelecionada: Rota | null = null;
  caminhaoFiltrado: Caminhao | null = null;

  agendamentos: { [dataISO: string]: Itinerario[] } = {};
  itinerariosDoDia: Itinerario[] = [];

  constructor(private itinerarioService: ItinerarioService) {}

  ngOnInit(): void {
    this.gerarCalendario();
    this.carregarAgendamentos();
  }

  alterarMes(delta: number): void {
    this.mesSelecionado += delta;
    if (this.mesSelecionado < 0) {
      this.mesSelecionado = 11;
      this.anoSelecionado--;
    } else if (this.mesSelecionado > 11) {
      this.mesSelecionado = 0;
      this.anoSelecionado++;
    }
    this.gerarCalendario();
  }

  gerarCalendario(): void {
    this.diasDoMes = [];
    this.diasVazios = [];
    const primeiroDia = new Date(this.anoSelecionado, this.mesSelecionado, 1);
    const ultimoDia = new Date(this.anoSelecionado, this.mesSelecionado + 1, 0);
    const diaSemana = primeiroDia.getDay();
    this.diasVazios = Array(diaSemana).fill(null);
    for (let d = 1; d <= ultimoDia.getDate(); d++) {
      this.diasDoMes.push(new Date(this.anoSelecionado, this.mesSelecionado, d));
    }
  }

  carregarAgendamentos(): void {
    this.itinerarioService.listar().subscribe(itens => {
      this.agendamentos = {};
      for (const it of itens) {
        const chave = this.gerarChaveData(new Date(it.data));
        if (!this.agendamentos[chave]) this.agendamentos[chave] = [];
        this.agendamentos[chave].push(it);
      }
    });
  }

  isHoje(dia: Date): boolean {
    const hoje = new Date();
    return dia.toDateString() === hoje.toDateString();
  }

  isSelecionado(dia: Date): boolean {
    return this.diaSelecionado?.toDateString() === dia.toDateString();
  }

  temAgendamento(dia: Date): boolean {
    const chave = this.gerarChaveData(dia);
    return this.agendamentos[chave]?.length > 0;
  }

  abrirFormulario(dia: Date): void {
    this.diaSelecionado = dia;
    this.diaConsultado = null;
    this.rotaSelecionada = null;
  }

  verAgendamentos(dia: Date): void {
    this.diaConsultado = dia;
    const chave = this.gerarChaveData(dia);
    this.itinerariosDoDia = this.agendamentos[chave] || [];
  }

  abrirModalRotas(): void {
    this.modalRotasVisivel = true;
    document.body.style.overflow = 'hidden';
  }

  fecharModalRotas(): void {
    this.modalRotasVisivel = false;
    document.body.style.overflow = '';
  }

  abrirModalCaminhao(): void {
    this.modalCaminhaoVisivel = true;
    document.body.style.overflow = 'hidden';
  }

  fecharModalCaminhao(): void {
    this.modalCaminhaoVisivel = false;
    document.body.style.overflow = '';
  }

  onRotaSelecionado(event: Rota): void {
    this.rotaSelecionada = event;
    this.fecharModalRotas();
  }

  onCaminhaoSelecionado(caminhao: Caminhao): void {
    this.caminhaoFiltrado = caminhao;
    this.fecharModalCaminhao();
  }

  agendar(): void {
    if (!this.diaSelecionado || !this.rotaSelecionada) return;
    const novo: Itinerario = {
      data: this.diaSelecionado,
      rota: this.rotaSelecionada
    };

    this.itinerarioService.salvar(novo).subscribe(() => {
      this.carregarAgendamentos();
      this.diaSelecionado = null;
      this.rotaSelecionada = null;
    });
  }

  excluir(itinerario: Itinerario): void {
    if (!itinerario.id) return;
    this.itinerarioService.excluir(itinerario.id.toString()).subscribe(() => {
      this.carregarAgendamentos();
      this.verAgendamentos(new Date(itinerario.data));
    });
  }

  editar(itinerario: Itinerario): void {
    this.diaSelecionado = new Date(itinerario.data);
    this.rotaSelecionada = itinerario.rota;
  }

  obterAgendamentosDoDia(dia: Date): Itinerario[] {
    const chave = this.gerarChaveData(dia);
    return this.agendamentos[chave] || [];
  }

  private gerarChaveData(dia: Date): string {
    return dia.toISOString().split('T')[0];
  }
  fecharAgendamento(): void {
    this.diaSelecionado = null;
    this.rotaSelecionada = null;
  }
}
