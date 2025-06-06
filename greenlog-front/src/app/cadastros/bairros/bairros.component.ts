import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Bairro } from './bairro.model';
import { BairroService } from './bairro.service';

import { TopbarComponent } from '../../padronizacao/menu/topbar/topbar.component';
import { SidebarComponent } from '../../padronizacao/menu/sidebar/sidebar.component';

@Component({
  selector: 'app-bairros',
  standalone: true,
  imports: [CommonModule, FormsModule, TopbarComponent, SidebarComponent],
  templateUrl: './bairros.component.html',
  styleUrls: ['./bairros.component.css']
})
export class BairrosComponent implements OnInit {

  bairros: Bairro[] = [];
  bairroAtual: Bairro = { nome: '' };
  idEditando: number | undefined = undefined;
  
  mensagemSucesso: string = '';
  mensagemErro: string = '';

  constructor(private bairroService: BairroService) { }

  ngOnInit(): void {
    this.carregarBairros();
  }

  carregarBairros(): void {
    this.bairroService.getBairros().subscribe(data => {
      this.bairros = data;
    });
  }

  salvar(): void {
    if (!this.bairroAtual.nome || this.bairroAtual.nome.trim() === '') {
      this.mostrarErro('Por favor, preencha o nome do bairro.');
      return;
    }

    if (this.idEditando) {
      // Atualizando
      this.bairroService.atualizarBairro(this.idEditando, this.bairroAtual).subscribe({
        next: () => {
          this.mostrarSucesso(`Bairro "${this.bairroAtual.nome}" atualizado com sucesso!`);
          this.resetForm();
        },
        error: (err) => this.mostrarErro(err.error?.message || 'Erro ao atualizar bairro.')
      });
    } else {
      // Criando
      this.bairroService.adicionarBairro(this.bairroAtual).subscribe({
        next: (novoBairro) => {
          this.mostrarSucesso(`Bairro "${novoBairro.nome}" salvo com sucesso!`);
          this.resetForm();
        },
        error: (err) => this.mostrarErro(err.error?.message || 'Erro ao salvar bairro.')
      });
    }
  }

  editar(bairro: Bairro): void {
    if (bairro.id) {
      this.idEditando = bairro.id;
      this.bairroAtual = { ...bairro }; 
    }
  }
  
  excluir(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Tem certeza que deseja excluir este bairro?')) {
      this.bairroService.excluirBairro(id).subscribe({
        next: () => {
          this.mostrarSucesso('Bairro excluído com sucesso!');
          this.carregarBairros();
        },
        error: (err) => this.mostrarErro(err.error?.message || 'Erro ao excluir bairro.')
      });
    }
  }
  resetForm(): void {
    this.bairroAtual = { nome: '' };
    this.idEditando = undefined;
    this.carregarBairros();
  }

  limparMensagens(): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';
  }

  mostrarErro(msg: string): void {
    this.limparMensagens();
    this.mensagemErro = msg;
  }

  mostrarSucesso(msg: string): void {
    this.limparMensagens();
    this.mensagemSucesso = msg;
  }
}