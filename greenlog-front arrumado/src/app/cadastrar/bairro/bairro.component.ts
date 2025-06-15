import { Component, OnInit } from '@angular/core';
import { Bairro } from './bairro.model';
import { BairroService } from './bairro.service';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { HandleErrorMessageService } from '../../handle-error-message.service';

@Component({
  selector: 'app-bairro',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './bairro.component.html',
  styleUrl: './bairro.component.css'
})
export class BairroComponent implements OnInit{
  
  bairros: Bairro[] = [];
  bairroAtual: Bairro = { nome: '' };
  idEditando: number | null = null;
  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = { tipo: null, texto: '' };
  
  constructor(
    private bairroService: BairroService,
    private handleErrorMessage: HandleErrorMessageService,
  ) {}

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    const textos = {
      salvo: ' Bairro cadastrado com sucesso!',
      editado: ' Bairro atualizado com sucesso!',
      excluido: ' Bairro excluído com sucesso!',
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
    this.bairroService.listar().subscribe({
      next: (res) => (this.bairros = res),
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
      this.bairroService.atualizar(this.idEditando, this.bairroAtual).subscribe(callback);
    } else {
      this.bairroService.salvar(this.bairroAtual).subscribe(callback);
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este bairro?');
    if (confirmar) {
      this.bairroService.excluir(id).subscribe({
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

  editar(bairro: Bairro): void {
    this.idEditando = bairro.id ?? null;
    this.bairroAtual = { ...bairro };
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  resetForm(form?: NgForm): void {
    this.bairroAtual = { nome: '' };
    this.idEditando = null;
    if (form) {
      form.resetForm();
    }
  }
}
