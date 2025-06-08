import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Caminhao } from './caminhao.modal';
import { CaminhaoService } from './caminhao.service';

@Component({
  selector: 'app-caminhao',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './caminhao.component.html',
  styleUrl: './caminhao.component.css'
})
export class CaminhaoComponent implements OnInit{

  caminhoes: Caminhao[] = [];
  caminhaoAtual: Caminhao = { placa: '', motorista: '', capacidade: 0, residuos: []};
  idEditando: number | null = null;

  tiposResiduosSelecionados: { [key: string]: boolean } = {};
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];
  
  mensagemSalvo = false;
  mensagemEditado = false;
  mensagemExcluido = false;
  mensagemErro = '';

  constructor(private caminhaoService: CaminhaoService) {}

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

  this.caminhaoAtual.residuos = this.opcoesTiposResiduos
    .filter(t => this.tiposResiduosSelecionados[t]);
  }

  buscarTodos(): void {
  this.caminhaoService.listar().subscribe({
    next: (res) => {
      this.caminhoes = res.map((caminhao) => ({
        ...caminhao,
        residuos: Array.isArray(caminhao.residuos)
          ? caminhao.residuos
          : (caminhao.residuos ? [caminhao.residuos] : [])
      }));
    },
    error: () => (this.mensagemErro = 'Erro ao buscar caminhões.'),
  });
}

  salvar(): void {
    this.limparMensagens();

    if (!this.validarCampos(this.caminhaoAtual)) return;

    if (this.idEditando) {
      this.caminhaoService.atualizar(this.idEditando, this.caminhaoAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao atualizar caminhao.'),
      });
    } else {
      this.caminhaoService.salvar(this.caminhaoAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao cadastrar caminhao.'),
      });
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este caminhao?');
    if (confirmar) {
      this.caminhaoService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao excluir caminhao.'),
      });
    }
  }

  editar(caminhao: Caminhao): void {
    this.idEditando = caminhao.id ?? null;
    this.caminhaoAtual = { ...caminhao };
    this.limparMensagens();
  }

  resetForm(): void {
    this.caminhaoAtual = {
      placa: '',
      motorista: '',
      capacidade: 0,
      residuos: []
    };
    this.idEditando = null;
    this.inicializarCheckboxes();
    this.limparMensagens();
  }

  limparMensagens(): void {
    this.mensagemErro = '';
  }

  validarCampos(caminhaoAtual: Caminhao): boolean {
    if (!caminhaoAtual.placa || caminhaoAtual.placa.trim() === '') {
      this.mensagemErro = 'A placa do caminhão é obrigatória.';
      return false;
    }

    if (!caminhaoAtual.motorista || caminhaoAtual.motorista.trim() === '') {
      this.mensagemErro = 'O nome do motorista é obrigatório.';
      return false;
    }

    if (
      caminhaoAtual.capacidade === null || 
      caminhaoAtual.capacidade === undefined || 
      caminhaoAtual.capacidade.toString().trim() === ''
    ) {
      this.mensagemErro = 'A capacidade do caminhão é obrigatória.';
      return false;
    }

    const algumSelecionado = Object.values(this.tiposResiduosSelecionados).some(valor => valor);

    if (!algumSelecionado) {
      this.mensagemErro = 'Selecione ao menos um tipo de resíduo.';
      return false;
    }

    return true;
  } 

  inicializarCheckboxes(): void {
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = false;
    });
  }

}