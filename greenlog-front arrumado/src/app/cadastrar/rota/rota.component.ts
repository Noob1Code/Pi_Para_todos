import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Rota } from './rota.model';
import { RotaService } from './rota.service';

import { ModalPontoColetaComponent } from "../../padronizador/modal/modal-ponto-coleta/modal-ponto-coleta.component"; 
import { ModalCaminhaoComponent } from "../../padronizador/modal/modal-caminhao/modal-caminhao.component";
import { Caminhao } from '../caminhao/caminhao.modal';
import { PontoColeta } from '../ponto-coleta/ponto-coleta.model';

@Component({
  selector: 'app-rota',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalPontoColetaComponent, ModalCaminhaoComponent],
  templateUrl: './rota.component.html',
  styleUrl: './rota.component.css'
})
export class RotaComponent implements OnInit {
  modalCaminhoesVisivel = false;
  modalPontosColetaVisivel = false;
  rotas: Rota[] = [];
  rotaAtual: Rota = {
    caminhao: null,
    destino: null,
    tipoResiduo: '',
    bairrosPercorridos: [],
    distanciaTotal: 0
  };
  idEditando: number | null = null;
  pontosColetaCompativeis: PontoColeta[] | null = null;

  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = {
    tipo: null,
    texto: ''
  };

  constructor(private rotaService: RotaService) {}
  
  ngOnInit(): void {
    this.buscarTodos();
  }
  
  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    this.mensagem = {
      tipo,
      texto:
        tipo === 'salvo' ? 'Rota cadastrada com sucesso!' :
        tipo === 'editado' ? 'Rota atualizada com sucesso!' :
        tipo === 'excluido' ? 'Rota excluída com sucesso!' :
        textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };
  
    if (tipo !== 'erro') {
      setTimeout(() => {
        this.mensagem = { tipo: null, texto: '' };
      }, 6000);
    }
  }

  buscarTodos(): void {
    this.rotaService.listar().subscribe({
      next: (res) => (this.rotas = res),
      error: () => (this.mostrarMensagem('erro','Erro ao buscar rotas.')),
    });
  }

  resetForm(form?: NgForm): void {
    form?.resetForm();
    this.rotaAtual = {
      caminhao: null,
      destino: null,
      tipoResiduo: '',
      bairrosPercorridos: [],
      distanciaTotal: 0
    };
    this.idEditando = null;
    this.pontosColetaCompativeis = null;
  }

  editar(rota: Rota): void {
    this.idEditando = rota.id ?? null;
    this.rotaAtual = { ...rota };
    if (rota.caminhao?.id) {
      this.carregarPontosCompativeis(rota.caminhao.id);
    }
  }

  // --- Lógica dos Modais ---

  abrirModalCaminhoes() {
    this.modalCaminhoesVisivel = true;
  }

  fecharModalCaminhoes() {
    this.modalCaminhoesVisivel = false;
  }

  onCaminhaoSelecionado(caminhao: Caminhao) {
    this.rotaAtual.caminhao = caminhao;
    this.rotaAtual.destino = null; // Limpa o destino para forçar nova seleção
    this.rotaAtual.tipoResiduo = ''; // Limpa o tipo de resíduo
    this.fecharModalCaminhoes();
    this.carregarPontosCompativeis(caminhao.id!);
  }
  
  abrirModalPontosColeta() {
    if (!this.rotaAtual.caminhao) {
      this.mostrarMensagem('erro', 'Por favor, selecione um caminhão primeiro.');
      return;
    }
    this.modalPontosColetaVisivel = true;
  }

  fecharModalPontosColeta() {
    this.modalPontosColetaVisivel = false;
  }
  
  onPontoColetaSelecionado(ponto: PontoColeta): void {
    this.rotaAtual.destino = ponto;
    this.fecharModalPontosColeta();
  }
  
  private carregarPontosCompativeis(caminhaoId: number) {
    this.pontosColetaCompativeis = null; // Mostra o "carregando" no modal
    this.rotaService.getPontosDeColetaCompativeis(caminhaoId).subscribe({
        next: (pontos) => {
            this.pontosColetaCompativeis = pontos;
        },
        error: () => {
            this.mostrarMensagem('erro', 'Erro ao buscar pontos de coleta compatíveis.');
            this.pontosColetaCompativeis = []; // Evita que fique em loading eterno
        }
    });
  }

  salvar(form: NgForm): void {
    if (this.idEditando) {
      this.rotaService.atualizar(this.idEditando, this.rotaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm(form);
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro', 'Erro ao atualizar rota.')),
      });
    } else {
      this.rotaService.salvar(this.rotaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm(form);
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro', 'Erro ao cadastrar rota.')),
      });
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir esta rota?');
    if (confirmar) {
      this.rotaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => this.mostrarMensagem('erro', 'Erro ao excluir rota.'),
      });
    }
  }

  calcularRota(): void {
    // O backend espera o ID do Bairro para calcular a rota
    const destinoBairroId = this.rotaAtual.destino?.bairro.id;

    if (!destinoBairroId) {
      this.mostrarMensagem('erro', 'Selecione um destino antes de calcular a rota.');
      return;
    }
    
    this.rotaService.calcularRota(destinoBairroId).subscribe({
      next: (res) => {
        this.rotaAtual.bairrosPercorridos = res.bairros.map(b => b.nome);
        this.rotaAtual.arestasPercorridas = res.arestas;
        this.rotaAtual.distanciaTotal = res.distanciaTotal;
      },
      error: () => this.mostrarMensagem('erro', 'Erro ao calcular rota.'),
    });
  }
}
