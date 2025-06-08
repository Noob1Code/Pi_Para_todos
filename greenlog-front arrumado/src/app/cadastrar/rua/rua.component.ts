import { Component, OnInit } from '@angular/core';
import { Rua } from './rua.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RuaService } from './rua.service';
import { ModalBairrosComponent } from "../../padronizador/modal/modal-bairro/modal-bairro.component";
import { Bairro } from '../bairro/bairro.model';

@Component({
  selector: 'app-rua',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalBairrosComponent],
  templateUrl: './rua.component.html',
  styleUrl: './rua.component.css'
})
export class RuaComponent implements OnInit{

  ruas: Rua[] = [];
  ruaAtual: Rua = {nome: '', origem: { id: 0, nome: '' }, destino: { id: 0, nome: '' }, distancia: 0}
  idEditando: number | null = null;

  modalOrigemVisivel = false;
  modalDestinoVisivel = false;

  mensagemSalvo = false;
  mensagemEditado = false;
  mensagemExcluido = false;
  mensagemErro = '';

  constructor(private ruaService : RuaService){}

  ngOnInit(): void {
    this.buscarTodos();
  }

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

  buscarTodos(): void {
    this.ruaService.listar().subscribe({
      next: (res) => (this.ruas = res),
      error: () => (this.mensagemErro = 'Erro ao buscar ruas.'),
    });
  }

  salvar(): void {
    this.limparMensagens();

    if (!this.validarCampos(this.ruaAtual)) return;
  
    if (this.idEditando) {
      this.ruaService.atualizar(this.idEditando, this.ruaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao atualizar rua.'),
      });
    } else {
      this.ruaService.salvar(this.ruaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao cadastrar rua.'),
      });
    }
  }
  
  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este rua?');
    if (confirmar) {
      this.ruaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao excluir rua.'),
      });
    }
  }
  
  editar(rua: Rua): void {
    this.idEditando = rua.id ?? null;
    this.ruaAtual = { ...rua };
    this.limparMensagens();
  }
  
  resetForm(): void {
    this.ruaAtual = {nome: '', origem: { id: 0, nome: '' }, destino: { id: 0, nome: '' }, distancia: 0}
    this.idEditando = null;
    this.limparMensagens();
  }
  
  limparMensagens(): void {
    this.mensagemErro = '';
  }

  validarCampos(ruaAtual: Rua): boolean {
    if (!ruaAtual.nome || ruaAtual.nome.trim() === '') {
      this.mensagemErro = 'O nome da Rua é obrigatória.';
      return false;
    }

    if (!ruaAtual.origem || ruaAtual.origem === null) {
      this.mensagemErro = 'A origem é obrigatório.';
      return false;
    }

    if (!ruaAtual.destino || ruaAtual.destino === null) {
      this.mensagemErro = 'O destino é obrigatório.';
      return false;
    }

    if (
      ruaAtual.distancia === null || 
      ruaAtual.distancia === undefined || 
      ruaAtual.distancia.toString().trim() === ''
    ) {
      this.mensagemErro = 'A distancia é obrigatória.';
      return false;
    }

    return true;
  }

  abrirModalOrigem(): void {
    this.modalOrigemVisivel = true;
    this.fecharModalDestino();
  }

  fecharModalOrigem(): void {
    this.modalOrigemVisivel = false;
  }

  onOrigemSelecionado(event: Bairro): void {
    this.ruaAtual.origem = event;
    this.modalOrigemVisivel = false;
  }

  abrirModalDestino(): void {
    this.modalDestinoVisivel = true;
    this.fecharModalOrigem();
  }

  fecharModalDestino(): void {
    this.modalDestinoVisivel = false;
  }

  onDestinoSelecionado(event: Bairro): void {
    this.ruaAtual.destino = event; 
    this.modalDestinoVisivel = false;
  } 
}
