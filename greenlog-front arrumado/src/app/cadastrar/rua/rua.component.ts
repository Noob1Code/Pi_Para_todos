import { Component, OnInit } from '@angular/core';
import { Rua } from './rua.model';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RuaService } from './rua.service';
import { ModalBairrosComponent } from "../../padronizador/modal/modal-bairro/modal-bairro.component";
import { Bairro } from '../bairro/bairro.model';
import { HandleErrorMessageService } from '../../handle-error-message.service';

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

  constructor(
    private ruaService : RuaService,
    private handleErrorMessage: HandleErrorMessageService,
  ) {}

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    const textos = {
      salvo: ' Rua cadastrado com sucesso!',
      editado: ' Rua atualizado com sucesso!',
      excluido: ' Rua excluído com sucesso!',
      erro: textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };
    window.scrollTo({ top: 0, behavior: 'smooth' });
    this.mensagem = { tipo, texto: textos[tipo] };
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
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    });
  }

  salvar(form: NgForm): void {
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
      this.ruaService.atualizar(this.idEditando, this.ruaAtual).subscribe(callback);
    } else {
      this.ruaService.salvar(this.ruaAtual).subscribe(callback);
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
        error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
        }
      });
    }
  }
  
  editar(rua: Rua): void {
    this.idEditando = rua.id ?? null;
    this.ruaAtual = { ...rua };
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
  
  resetForm(form: NgForm): void {
    this.ruaAtual = {nome: '', origem: { id: 0, nome: '' }, destino: { id: 0, nome: '' }, distancia: null}
    this.idEditando = null;
    if (form) {
      form.resetForm();
    }
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
