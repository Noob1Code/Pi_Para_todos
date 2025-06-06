// IMPORTANTE: Adicionar a importação do modelo Bairro
import { Bairro } from '../bairros/bairro.model'; 
// IMPORTANTE: Corrigir a importação do componente do modal
import { ModalBairrosComponent } from '../../padronizacao/modal/modal-bairros/modal-bairros.component';

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PontoColeta } from './ponto-coleta.model';
import { PontoColetaService } from './ponto-coleta.service';
import { TopbarComponent } from "../../padronizacao/menu/topbar/topbar.component";
import { SidebarComponent } from "../../padronizacao/menu/sidebar/sidebar.component";
// A importação antiga do BairrosComponent não é mais necessária

@Component({
  selector: 'app-pontos-coleta',
  standalone: true,
  // ALTERADO: Corrigido o componente importado para o modal correto
  imports: [CommonModule, FormsModule, TopbarComponent, SidebarComponent, ModalBairrosComponent], 
  templateUrl: './pontos-coleta.component.html',
  styleUrls: ['./pontos-coleta.component.css']
})
export class PontosColetaComponent implements OnInit {

  pontosColeta: PontoColeta[] = [];
  pontoColetaAtual: PontoColeta = this.getCleanPontoColeta();
  idEditando: number | null = null;
  
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];
  tiposResiduosSelecionados: { [key: string]: boolean } = {};

  constructor(
    private pontoColetaService: PontoColetaService
  ) {}

  ngOnInit(): void {
    this.carregarPontosColeta();
    this.resetTiposResiduosSelecionados();
  }
  
  private getCleanPontoColeta(): PontoColeta {
    return {
      // ALTERADO: a propriedade 'bairroId' foi corrigida para 'bairro'
      bairro: null, 
      nome: '',
      responsavel: '',
      telefoneResponsavel: '',
      emailResponsavel: '',
      endereco: '',
      horarioFuncionamento: '',
      tiposResiduosAceitos: []
    };
  }

  private resetTiposResiduosSelecionados(): void {
    this.tiposResiduosSelecionados = {};
    this.opcoesTiposResiduos.forEach(tipo => this.tiposResiduosSelecionados[tipo] = false);
  }

  private atualizarTiposSelecionadosComBaseNoModelo(): void {
    this.resetTiposResiduosSelecionados();
    if (this.pontoColetaAtual?.tiposResiduosAceitos) {
      this.pontoColetaAtual.tiposResiduosAceitos.forEach(tipoAceito => {
        if (this.opcoesTiposResiduos.includes(tipoAceito)) {
          this.tiposResiduosSelecionados[tipoAceito] = true;
        }
      });
    }
  }

  onTipoResiduoChange(tipo: string, event: Event): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    this.tiposResiduosSelecionados[tipo] = isChecked;
    this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
      .filter(t => this.tiposResiduosSelecionados[t]);
  }

  carregarPontosColeta(): void {
    this.pontoColetaService.getPontosColeta().subscribe({
      next: (data) => this.pontosColeta = data,
      error: (err) => alert('Erro ao carregar pontos de coleta: ' + err.message)
    });
  }

  salvar(): void {
    // ALTERADO: a validação agora checa o objeto 'bairro', e não mais 'bairroId'
    if (!this.pontoColetaAtual.bairro) { 
      alert('O Bairro é obrigatório.');
      return;
    }
    // O resto das validações permanece igual
    if (!this.pontoColetaAtual.nome || this.pontoColetaAtual.nome.length < 3) {
      alert('O Nome do Ponto é obrigatório e deve ter no mínimo 3 caracteres.');
      return;
    }
    if (!this.pontoColetaAtual.responsavel || this.pontoColetaAtual.responsavel.length < 3) {
      alert('O Responsável é obrigatório e deve ter no mínimo 3 caracteres.');
      return;
    }
    if (!this.pontoColetaAtual.emailResponsavel || !this.validarEmail(this.pontoColetaAtual.emailResponsavel)) {
        alert('Por favor, informe um e-mail válido.');
        return;
    }
    this.pontoColetaAtual.tiposResiduosAceitos = this.opcoesTiposResiduos
      .filter(t => this.tiposResiduosSelecionados[t]);

    if (this.pontoColetaAtual.tiposResiduosAceitos.length === 0) {
        alert('Selecione pelo menos um tipo de resíduo.');
        return;
    }
    
    // Nenhuma alteração necessária aqui, o objeto já está no formato correto
    const dadosParaSalvar: PontoColeta = { ...this.pontoColetaAtual };
    if (this.idEditando === null) {
      delete dadosParaSalvar.id;
      this.pontoColetaService.adicionarPontoColeta(dadosParaSalvar).subscribe({
        next: () => {
          alert('Ponto de coleta salvo com sucesso!');
          this.finalizarEdicao();
        },
        error: (err) => alert('Erro ao salvar: ' + err.message)
      });
    } else {
      this.pontoColetaService.atualizarPontoColeta(this.idEditando, dadosParaSalvar).subscribe({
        next: () => {
          alert('Ponto de coleta atualizado com sucesso!');
          this.finalizarEdicao();
        },
        error: (err) => alert('Erro ao atualizar: ' + err.message)
      });
    }
  }

  editarPontoColeta(ponto: PontoColeta): void {
    if (ponto.id === undefined) {
      alert("ID do ponto de coleta não encontrado para edição.");
      return;
    }
    this.idEditando = ponto.id;
    this.pontoColetaAtual = JSON.parse(JSON.stringify(ponto));
    this.atualizarTiposSelecionadosComBaseNoModelo();
  }

  excluirPontoColeta(id: number | undefined): void {
    if (id === undefined) {
      alert("ID do ponto de coleta não encontrado para exclusão.");
      return;
    }
    if (confirm('Tem certeza que deseja excluir este ponto de coleta?')) {
      this.pontoColetaService.excluirPontoColeta(id).subscribe({
        next: () => {
          alert('Ponto de coleta excluído com sucesso!');
          this.carregarPontosColeta();
          if (this.idEditando === id) {
            this.finalizarEdicao();
          }
        },
        error: (err) => alert('Erro ao excluir: ' + err.message)
      });
    }
  }

  finalizarEdicao(): void {
    this.pontoColetaAtual = this.getCleanPontoColeta();
    this.idEditando = null;
    this.resetTiposResiduosSelecionados();
    this.carregarPontosColeta();
  }

  validarEmail(email: string): boolean {
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regexEmail.test(email);
  }

  // --- MÉTODOS PARA CONTROLE DO MODAL ---
  
  mostrarModalBairros = false;

  abrirModalBairros() {
    this.mostrarModalBairros = true;
  }

  fecharModalBairros() {
    this.mostrarModalBairros = false;
  }
  
  // NOVO: Método para receber o bairro selecionado que vem do modal
  selecionarBairro(bairroSelecionado: Bairro): void {
    this.pontoColetaAtual.bairro = bairroSelecionado;
    this.fecharModalBairros(); // Fecha o modal após a seleção
  }
}