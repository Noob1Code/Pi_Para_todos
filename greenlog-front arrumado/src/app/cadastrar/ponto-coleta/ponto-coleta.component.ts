import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PontoColeta } from './ponto-coleta.model';
import { PontoColetaService } from './ponto-coleta.service';
import { ModalBairrosComponent } from '../../padronizador/modal/modal-bairro/modal-bairro.component';
import { Bairro } from '../bairro/bairro.model';

@Component({
  selector: 'app-ponto-coleta',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalBairrosComponent],
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

  idEditando: number | null = null;
  modalBairrosVisivel = false;

  tiposResiduosSelecionados: { [key: string]: boolean } = {};
  erroResiduos = false;
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];

  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = {
    tipo: null,
    texto: ''
  };

  constructor(private pontoColetaService: PontoColetaService) {}

  ngOnInit(): void {
    this.inicializarCheckboxes();
    this.buscarTodos();
  }

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    this.mensagem = {
      tipo,
      texto:
        tipo === 'salvo' ? 'Ponto de coleta cadastrado com sucesso!' :
        tipo === 'editado' ? 'Ponto de coleta atualizado com sucesso!' :
        tipo === 'excluido' ? 'Ponto de coleta excluído com sucesso!' :
        textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };

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
      error: () => this.mostrarMensagem('erro', 'Erro ao buscar pontos de coleta.'),
    });
  }

  salvar(): void {

    const algumSelecionado = Object.values(this.tiposResiduosSelecionados).some(v => v === true);

    if (!algumSelecionado) {
    this.erroResiduos = true;
    return;
  }

  this.erroResiduos = false;

    if (this.idEditando) {
      this.pontoColetaService.atualizar(this.idEditando, this.pontoColetaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => this.mostrarMensagem('erro', 'Erro ao atualizar ponto de coleta.'),
      });
    } else {
      this.pontoColetaService.salvar(this.pontoColetaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => this.mostrarMensagem('erro', 'Erro ao cadastrar ponto de coleta.'),
      });
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
        error: () => this.mostrarMensagem('erro', 'Erro ao excluir ponto de coleta.'),
      });
    }
  }

  editar(ponto: PontoColeta): void {
    this.idEditando = ponto.id ?? null;
    this.pontoColetaAtual = { ...ponto };

    this.tiposResiduosSelecionados = {};
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = ponto.tiposResiduosAceitos.includes(tipo);
    });

    this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
      .filter(t => this.tiposResiduosSelecionados[t]);
  }

  resetForm(): void {
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
    this.idEditando = null;
    this.inicializarCheckboxes();
  }

  inicializarCheckboxes(): void {
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = false;
    });
  }

  erro(mensagem: string): boolean {
    this.mostrarMensagem('erro', mensagem);
    return false;
  }

  abrirModalBairros(): void {
    this.modalBairrosVisivel = true;
  }

  fecharModalBairros(): void {
    this.modalBairrosVisivel = false;
  }

  onBairroSelecionado(event: Bairro): void {
    this.pontoColetaAtual.bairro = { id: event.id, nome: event.nome };
    this.modalBairrosVisivel = false;
  }

  nenhumResiduoSelecionado(): boolean {
  return !Object.values(this.tiposResiduosSelecionados).some(v => v === true);
}
}
