import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { PontoColeta } from './ponto-coleta.model';
import { PontoColetaService } from './ponto-coleta.service';
import { ModalBairrosComponent } from '../../padronizador/modal/modal-bairro/modal-bairro.component';
import { Bairro } from '../bairro/bairro.model';
import { HandleErrorMessageService } from '../../handle-error-message.service';
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: 'app-ponto-coleta',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalBairrosComponent,NgxMaskDirective],
  templateUrl: './ponto-coleta.component.html',
  styleUrl: './ponto-coleta.component.css'
})
export class PontoColetaComponent implements OnInit {

  pontosColeta: PontoColeta[] = [];
  pontoColetaAtual: PontoColeta = {
    nome: '',
    responsavel: '',
    telefoneResponsavel: '',
    emailResponsavel: '',
    endereco: '',
    horarioFuncionamento: '',
    bairro: { id: 0, nome: '' },
    tiposResiduosAceitos: []
  };
  centro = 'Centro';
  idEditando: number | null = null;
  modalBairrosVisivel = false;
  tiposResiduosSelecionados: { [key: string]: boolean } = {};
  erroResiduos = false;
  telefoneMask: string = '(00) 0000-0000';
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];
  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = {
    tipo: null,
    texto: ''
  };

  constructor(
    private pontoColetaService: PontoColetaService,
    private handleErrorMessage: HandleErrorMessageService,
  ) {}

  ngOnInit(): void {
    this.telefoneMask = '(00) 00000-0000';
    this.inicializarCheckboxes();
    this.buscarTodos();
  }

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    const textos = {
      salvo: ' Ponto de Coleta cadastrado com sucesso!',
      editado: ' Ponto de Coleta atualizado com sucesso!',
      excluido: ' Ponto de Coleta excluído com sucesso!',
      erro: textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };
    this.mensagem = { tipo, texto: textos[tipo] };
    if (tipo !== 'erro') {
    setTimeout(() => {
    this.mensagem = { tipo: null, texto: '' };
    }, 6000);
    }
  }

  onTipoResiduoChange(tipo: string, isChecked: boolean): void {
    this.tiposResiduosSelecionados[tipo] = isChecked;
    this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
      .filter(t => this.tiposResiduosSelecionados[t]);
  }

  buscarTodos(): void {
    this.pontoColetaService.listar().subscribe({
      next: (res) => {
        this.pontosColeta = res.map((p) => ({
          ...p,
          tiposResiduosAceitos: Array.isArray(p.tiposResiduosAceitos)
            ? p.tiposResiduosAceitos
            : (p.tiposResiduosAceitos ? [p.tiposResiduosAceitos] : [])
        }));
      },
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    });
  }

  salvar(form: NgForm): void {
    const horario = this.pontoColetaAtual.horarioFuncionamento;

    if (!this.validarHorario(horario!)) {
      this.mensagem = { tipo: 'erro', texto: 'Horário inválido! Use o formato HH:MM - HH:MM e garanta que o horário inicial seja menor que o final.' };
      return;
    }

    const algumSelecionado = Object.values(this.tiposResiduosSelecionados).some(v => v === true);
    if (!algumSelecionado) {
      this.erroResiduos = true;
      return;
    }
    this.erroResiduos = false;
    
    const callback = {
      next: () => {
        this.mostrarMensagem(this.idEditando ? 'editado' : 'salvo');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    };

    if (this.idEditando) {
      this.pontoColetaService.atualizar(this.idEditando, this.pontoColetaAtual).subscribe(callback);
    } else {
      this.pontoColetaService.salvar(this.pontoColetaAtual).subscribe(callback);
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este ponto de coleta?');
    if (confirmar) {
      this.pontoColetaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
        }
      });
    }
  }

  editar(ponto: PontoColeta): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.idEditando = ponto.id ?? null;
    this.pontoColetaAtual = { ...ponto };
    this.tiposResiduosSelecionados = {};
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = ponto.tiposResiduosAceitos.includes(tipo);
    });

    this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
      .filter(t => this.tiposResiduosSelecionados[t]);
  }

  resetForm(form?: NgForm): void {
    this.pontoColetaAtual = {
      nome: '',
      responsavel: '',
      telefoneResponsavel: '',
      emailResponsavel: '',
      endereco: '',
      horarioFuncionamento: '',
      bairro: { id: 0, nome: '' },
      tiposResiduosAceitos: []
    };
    if (form) {
      form.resetForm();
    }
    this.idEditando = null;
    this.inicializarCheckboxes();
  }

  inicializarCheckboxes(): void {
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = false;
    });
  }

  abrirModalBairros(): void {
    this.modalBairrosVisivel = true;
    document.body.style.overflow = 'hidden';
  }

  fecharModalBairros(): void {
    this.modalBairrosVisivel = false;
    document.body.style.overflow = '';
  }

  onBairroSelecionado(event: Bairro): void {
    this.pontoColetaAtual.bairro = { id: event.id, nome: event.nome };
    this.fecharModalBairros();
  }

  nenhumResiduoSelecionado(): boolean {
  return !Object.values(this.tiposResiduosSelecionados).some(v => v === true);
  }

  validarHorario(horario: string): boolean {
    const regex = /^([01]\d|2[0-3]):([0-5]\d)\s\-\s([01]\d|2[0-3]):([0-5]\d)$/;
    if (!regex.test(horario)) {
      return false;
    }
    const [inicio, fim] = horario.split(' - ');
    const [horaInicio, minutoInicio] = inicio.split(':').map(Number);
    const [horaFim, minutoFim] = fim.split(':').map(Number);
    const totalMinutosInicio = horaInicio * 60 + minutoInicio;
    const totalMinutosFim = horaFim * 60 + minutoFim;
    return totalMinutosInicio < totalMinutosFim;
  }

  onTelefoneChange(valor: string) {
    if (!valor) return;
    const digitsOnly = valor.replace(/\D/g, '');
    this.telefoneMask = digitsOnly.length > 10 ? '(00) 00000-0000' : '(00) 0000-0000';
  }
}
