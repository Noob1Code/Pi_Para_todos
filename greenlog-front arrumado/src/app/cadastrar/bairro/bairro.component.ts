import { Component, OnInit } from '@angular/core';
import { Bairro } from './bairro.model';
import { BairroService } from './bairro.service';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

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

  mensagemSalvo = false;
  mensagemEditado = false;
  mensagemExcluido = false;
  mensagemErro = '';

  constructor(private bairroService: BairroService) {}

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
    this.buscarTodos();
  }

  buscarTodos(): void {
    this.bairroService.listar().subscribe({
      next: (res) => (this.bairros = res),
      error: () => (this.mensagemErro = 'Erro ao buscar bairros.'),
    });
  }

  salvar(form: NgForm): void {
  this.limparMensagens();

  if (this.idEditando) {
    this.bairroService.atualizar(this.idEditando, this.bairroAtual).subscribe({
      next: () => {
        this.mostrarMensagem('editado');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: () => (this.mensagemErro = 'Erro ao atualizar bairro.'),
    });
  } else {
    this.bairroService.salvar(this.bairroAtual).subscribe({
      next: () => {
        this.mostrarMensagem('salvo');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: () => (this.mensagemErro = 'Erro ao cadastrar bairro.'),
    });
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
        error: () => (this.mensagemErro = 'Erro ao excluir bairro.'),
      });
    }
  }

  editar(bairro: Bairro): void {
    this.idEditando = bairro.id ?? null;
    this.bairroAtual = { ...bairro };
    this.limparMensagens();
  }

  resetForm(form?: NgForm): void {
  this.bairroAtual = { nome: '' };
  this.idEditando = null;
  this.limparMensagens();

  if (form) {
    form.resetForm(); // <-- Isso limpa os estados touched, dirty, etc.
  }
}

  limparMensagens(): void {
    this.mensagemErro = '';
  }

}
