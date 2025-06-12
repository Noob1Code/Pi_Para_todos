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
  ruaAtual: Rua = {nome: '', origem: { id: 0, nome: '' }, destino: { id: 0, nome: '' }, distancia: null}
  idEditando: number | null = null;

  modalOrigemVisivel = false;
  modalDestinoVisivel = false;

  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = { tipo: null, texto: '' };

  constructor(private ruaService : RuaService){}

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
  this.mensagem = {
    tipo,
    texto:
      tipo === 'salvo' ? ' Bairro cadastrado com sucesso!' :
      tipo === 'editado' ? ' Bairro atualizado com sucesso!' :
      tipo === 'excluido' ? ' Bairro excluído com sucesso!' :
      textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
  };

  if (tipo !== 'erro') {
  setTimeout(() => {
    this.mensagem = { tipo: null, texto: '' };
  }, 6000);
}
}

  ngOnInit(): void {
    this.buscarTodos();
  }

  buscarTodos(): void {
    this.ruaService.listar().subscribe({
      next: (res) => (this.ruas = res),
      error: () => (this.mostrarMensagem('erro','Erro ao buscar ruas.')),
    });
  }

  salvar(): void {
  
    if (this.idEditando) {
      this.ruaService.atualizar(this.idEditando, this.ruaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro','Erro ao atualizar rua.')),
      });
    } else {
      this.ruaService.salvar(this.ruaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro','Erro ao cadastrar rua.')),
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
        error: () => (this.mostrarMensagem('erro','Erro ao excluir rua.')),
      });
    }
  }
  
  editar(rua: Rua): void {
    this.idEditando = rua.id ?? null;
    this.ruaAtual = { ...rua };

  }
  
  resetForm(): void {
    this.ruaAtual = {nome: '', origem: { id: 0, nome: '' }, destino: { id: 0, nome: '' }, distancia: null}
    this.idEditando = null;

  }
  
  abrirModalOrigem(): void {
    this.modalOrigemVisivel = true;
    document.body.style.overflow = 'hidden';
    this.modalDestinoVisivel = false;
  }

  fecharModalOrigem(): void {
    this.modalOrigemVisivel = false;
    document.body.style.overflow = '';
  }

  onOrigemSelecionado(event: Bairro): void {
    this.ruaAtual.origem = event;
    this.fecharModalOrigem();
  }

  abrirModalDestino(): void {
    this.modalDestinoVisivel = true;
    document.body.style.overflow = 'hidden';
    this.modalOrigemVisivel = false;
  }

  fecharModalDestino(): void {
    this.modalDestinoVisivel = false;
    document.body.style.overflow = '';
  }

  onDestinoSelecionado(event: Bairro): void {
    this.ruaAtual.destino = event; 
    this.fecharModalDestino();
  } 
}
