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
  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = { tipo: null, texto: '' };
  
  constructor(private bairroService: BairroService) {}

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
    this.bairroService.listar().subscribe({
      next: (res) => (this.bairros = res),
      error: () => (this.mostrarMensagem('erro','Erro ao buscar bairros.')),
    });
  }

  salvar(form: NgForm): void {

  if (this.idEditando) {
    this.bairroService.atualizar(this.idEditando, this.bairroAtual).subscribe({
      next: () => {
        this.mostrarMensagem('editado');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: () => (this.mostrarMensagem('erro','Erro ao atualizar bairro.')),
    });
  } else {
    this.bairroService.salvar(this.bairroAtual).subscribe({
      next: () => {
        this.mostrarMensagem('salvo');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: () => (this.mostrarMensagem('erro','Erro ao cadastrar bairro.')),
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
        error: () => (this.mostrarMensagem('erro','Erro ao excluir bairro.')),
      });
    }
  }

  editar(bairro: Bairro): void {
    this.idEditando = bairro.id ?? null;
    this.bairroAtual = { ...bairro };
  }

  resetForm(form?: NgForm): void {
  this.bairroAtual = { nome: '' };
  this.idEditando = null;
  if (form) {
    form.resetForm(); // <-- Isso limpa os estados touched, dirty, etc.
  }
}
}
