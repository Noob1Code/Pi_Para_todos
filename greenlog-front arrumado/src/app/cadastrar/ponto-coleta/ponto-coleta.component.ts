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
export class PontoColetaComponent implements OnInit{
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

  modalBairrosVisivel = false;
  idEditando: number | null = null;
  tiposResiduosSelecionados: { [key: string]: boolean } = {};
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];
  
  mensagemSalvo = false;
  mensagemEditado = false;
  mensagemExcluido = false;
  mensagemErro = '';

  constructor(private pontoColetaService: PontoColetaService) {}

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido') {
    if (tipo === 'salvo') this.mensagemSalvo = true;
    if (tipo === 'editado') this.mensagemEditado = true;
    if (tipo === 'excluido') this.mensagemExcluido = true;

    setTimeout(() => {
      this.mensagemSalvo = false;
      this.mensagemEditado = false;
      this.mensagemExcluido = false;
    }, 3000);
  }

  ngOnInit(): void {
    this.inicializarCheckboxes();
    this.buscarTodos();
  }

  onTipoResiduoChange(tipo: string, isChecked: boolean): void {
  this.tiposResiduosSelecionados[tipo] = isChecked;

  this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
    .filter(t => this.tiposResiduosSelecionados[t]);
  }

  buscarTodos(): void {
  this.pontoColetaService.listar().subscribe({
    next: (res) => {
      this.pontosColeta = res.map((pontoColeta) => ({
        ...pontoColeta,
        tiposResiduosAceitos: Array.isArray(pontoColeta.tiposResiduosAceitos)
          ? pontoColeta.tiposResiduosAceitos
          : (pontoColeta.tiposResiduosAceitos ? [pontoColeta.tiposResiduosAceitos] : [])
      }));
    },
    error: () => (this.mensagemErro = 'Erro ao buscar os Pontos de Coletas.'),
  });
}

  salvar(): void {
    this.limparMensagens();

    if (!this.validarCampos(this.pontoColetaAtual)) return;

    if (this.idEditando) {
      this.pontoColetaService.atualizar(this.idEditando, this.pontoColetaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao atualizar pontoColeta.'),
      });
    } else {
      this.pontoColetaService.salvar(this.pontoColetaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao cadastrar pontoColeta.'),
      });
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este pontoColeta?');
    if (confirmar) {
      this.pontoColetaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao excluir pontoColeta.'),
      });
    }
  }

  editar(pontoColeta: PontoColeta): void {
    this.idEditando = pontoColeta.id ?? null;
    this.pontoColetaAtual = { ...pontoColeta };
    this.limparMensagens();
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
    this.limparMensagens();
  }

  limparMensagens(): void {
    this.mensagemErro = '';
  }

  validarCampos(pontoColetaAtual: PontoColeta): boolean {
    if (!pontoColetaAtual.nome || pontoColetaAtual.nome.trim() === '') {
      this.mensagemErro = 'O nome do ponto de coleta é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.responsavel || pontoColetaAtual.responsavel.trim() === '') {
      this.mensagemErro = 'O nome do responsável é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.telefoneResponsavel || pontoColetaAtual.telefoneResponsavel.trim() === '') {
      this.mensagemErro = 'O telefone do responsável é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.emailResponsavel || pontoColetaAtual.emailResponsavel.trim() === '') {
      this.mensagemErro = 'O e-mail do responsável é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.endereco || pontoColetaAtual.endereco.trim() === '') {
      this.mensagemErro = 'O endereço do ponto de coleta é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.horarioFuncionamento || pontoColetaAtual.horarioFuncionamento.trim() === '') {
      this.mensagemErro = 'O horário de funcionamento é obrigatório.';
      return false;
    }

    if (!pontoColetaAtual.bairro || pontoColetaAtual.bairro.nome === null) {
      this.mensagemErro = 'O bairro é obrigatório.';
      return false;
    }

    const algumSelecionado = pontoColetaAtual.tiposResiduosAceitos && pontoColetaAtual.tiposResiduosAceitos.length > 0;
    if (!algumSelecionado) {
      this.mensagemErro = 'Selecione ao menos um tipo de resíduo aceito.';
      return false;
    }

    return true;
  }

  inicializarCheckboxes(): void {
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = false;
    });
  }

  abrirModalBairros(): void {
    this.modalBairrosVisivel = true;
  }

  fecharModalBairros(): void {
    this.modalBairrosVisivel = false;
  }

  onBairroSelecionado(event: Bairro): void {
    this.pontoColetaAtual.bairro.id = event.id;
    this.pontoColetaAtual.bairro.nome = event.nome;
    this.modalBairrosVisivel = false;
  }

}
