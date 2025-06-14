import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ModalRotaComponent } from "../padronizador/modal/modal-rota/modal-rota.component";
import { ModalCaminhaoComponent } from "../padronizador/modal/modal-caminhao/modal-caminhao.component";
import { Rota } from '../cadastrar/rota/rota.model';
import { Caminhao } from '../cadastrar/caminhao/caminhao.modal';
import { ItinerarioService } from './itinerario.service';
import { Itinerario } from './itinerario.model';
import { HandleErrorMessageService } from '../handle-error-message.service';

@Component({
  standalone: true,
  selector: 'app-itinerario',
  imports: [CommonModule, FormsModule, ModalRotaComponent, ModalCaminhaoComponent],
  templateUrl: './itinerario.component.html',
  styleUrl: './itinerario.component.css'
})
export class ItinerarioComponent implements OnInit {
  diasDaSemana = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
  mesSelecionado = new Date().getMonth();
  anoSelecionado = new Date().getFullYear();
  diasDoMes: Date[] = [];
  diasVazios: any[] = [];
  diaConsultado: Date | null = null;
  diaSelecionado: Date | null = null;
  modalRotasVisivel = false;
  modalCaminhaoVisivel = false;
  rotaSelecionada: Rota | null = null;
  nomeDestinoSelecionado = '';
  caminhaoFiltrado: Caminhao | null = null;
  agendamentos: { [dataISO: string]: Itinerario[] } = {};
  itinerariosDoDia: Itinerario[] = [];
  idEditando: number | null = null;
  mensagem = { tipo: null as 'salvo' | 'editado' | 'excluido' | 'erro' | null, texto: '' };

  constructor(
  private itinerarioService: ItinerarioService,
  private handleErrorMessage: HandleErrorMessageService,
  ) {}
  
  ngOnInit(): void {
    this.gerarCalendario();
    this.carregarAgendamentos();
  }

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    const textos = {
      salvo: 'Itinerário agendado com sucesso!',
      editado: 'Itinerário atualizado com sucesso!',
      excluido: 'Itinerário excluído com sucesso!',
      erro: textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };
    this.mensagem = { tipo, texto: textos[tipo] };
    if (tipo !== 'erro') {
    setTimeout(() => {
    this.mensagem = { tipo: null, texto: '' };
    }, 6000);
    }
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
    const primeiroDia = new Date(this.anoSelecionado, this.mesSelecionado, 1);
    const ultimoDia = new Date(this.anoSelecionado, this.mesSelecionado + 1, 0);
    const diaSemana = primeiroDia.getDay();

    this.diasVazios = Array(diaSemana).fill(null);
    this.diasDoMes = Array.from({ length: ultimoDia.getDate() }, (_, i) =>
      new Date(this.anoSelecionado, this.mesSelecionado, i + 1)
    );
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
    return dia.toDateString() === new Date().toDateString();
  }

  isSelecionado(dia: Date): boolean {
    return this.diaSelecionado?.toDateString() === dia.toDateString();
  }

  temAgendamento(dia: Date): boolean {
    const chave = this.gerarChaveData(dia);
    const itinerarios = this.agendamentos[chave] || [];

    if (this.caminhaoFiltrado) {
      return itinerarios.some(it => it.rota?.caminhao?.id === this.caminhaoFiltrado?.id);
    }
    return itinerarios.length > 0;
  }
  
  abrirFormulario(dia: Date): void {
    this.diaSelecionado = dia;
    this.diaConsultado = null;
    this.rotaSelecionada = null;
    this.nomeDestinoSelecionado = '';
    this.idEditando = null;
  }

  verAgendamentos(dia: Date): void {
    this.diaConsultado = dia;
    this.itinerariosDoDia = this.obterAgendamentosDoDia(dia);
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

  onRotaSelecionado(rota: Rota): void {
    this.rotaSelecionada = rota;
    this.nomeDestinoSelecionado = rota.destino?.nome || '';
    this.fecharModalRotas();
  }

  onCaminhaoSelecionado(caminhao: Caminhao): void {
    this.caminhaoFiltrado = caminhao;
    this.fecharModalCaminhao();
  }

  salvar(): void {
    if (!this.rotaSelecionada || !this.diaSelecionado) {
      this.mostrarMensagem('erro', 'Selecione uma data e uma rota para salvar.');
      return;
    }

    const novo: Itinerario = {
      data: this.diaSelecionado,
      rota: this.rotaSelecionada
    };

    const callback = {
      next: () => {
        this.carregarAgendamentos();
        this.mostrarMensagem(this.idEditando ? 'editado' : 'salvo');
        this.fecharAgendamento();
      },
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    };

    if (this.idEditando) {
      this.itinerarioService.atualizar(this.idEditando, novo).subscribe(callback);
    } else {
      this.itinerarioService.salvar(novo).subscribe(callback);
    }
  }


  excluir(itinerario: Itinerario): void {
    if (!itinerario.id) return;

    this.itinerarioService.excluir(itinerario.id.toString()).subscribe({
      next: () => {
        this.carregarAgendamentos();
        this.mostrarMensagem('excluido');
        this.verAgendamentos(new Date(itinerario.data));
      },
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    });
  }

  editar(itinerario: Itinerario): void {
    this.idEditando = itinerario.id || null;
    this.diaSelecionado = new Date(itinerario.data);
    this.rotaSelecionada = itinerario.rota;
    this.nomeDestinoSelecionado = itinerario.rota?.destino?.nome || '';
  }

  obterAgendamentosDoDia(dia: Date): Itinerario[] {
    const chave = this.gerarChaveData(dia);
    const todos = this.agendamentos[chave] || [];

    return this.caminhaoFiltrado
      ? todos.filter(it => it.rota?.caminhao?.id === this.caminhaoFiltrado!.id)
      : todos;
  }

  private gerarChaveData(dia: Date): string {
    return dia.toISOString().split('T')[0];
  }

  fecharAgendamento(): void {
    this.diaSelecionado = null;
    this.rotaSelecionada = null;
    this.nomeDestinoSelecionado = '';
    this.idEditando = null;
  }
  limparFiltroCaminhao(): void {
  this.caminhaoFiltrado = null;
  }
}