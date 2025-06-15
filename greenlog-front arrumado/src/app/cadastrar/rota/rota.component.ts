import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Rota } from './rota.model';
import { RotaService } from './rota.service';
import { ModalPontoColetaComponent } from "../../padronizador/modal/modal-ponto-coleta/modal-ponto-coleta.component"; 
import { ModalCaminhaoComponent } from "../../padronizador/modal/modal-caminhao/modal-caminhao.component";
import { Caminhao } from '../caminhao/caminhao.modal';
import { PontoColeta } from '../ponto-coleta/ponto-coleta.model';
import { HandleErrorMessageService } from '../../handle-error-message.service';

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
  caminhaoid: number | null = null;
  filtroResiduo = '';
  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = {
    tipo: null,
    texto: ''
  };

  constructor(
    private rotaService: RotaService,
    private handleErrorMessage: HandleErrorMessageService,
  ) {}
  
  ngOnInit(): void {
    this.buscarTodos();
  }
  
  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    const textos = {
      salvo: ' Rota cadastrado com sucesso!',
      editado: ' Rota atualizado com sucesso!',
      excluido: ' Rota excluído com sucesso!',
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

  buscarTodos(): void {
    this.rotaService.listar().subscribe({
      next: (res) => (this.rotas = res),
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
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
      this.caminhaoid  = rota.caminhao.id;
    }
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  abrirModalCaminhoes() {
    this.modalCaminhoesVisivel = true;  
    document.body.style.overflow = 'hidden';
  }

  fecharModalCaminhoes() {
    this.modalCaminhoesVisivel = false;
    document.body.style.overflow = '';
  }

  onCaminhaoSelecionado(caminhao: Caminhao) {
    this.rotaAtual.caminhao = caminhao;
    this.rotaAtual.destino = null;
    this.rotaAtual.tipoResiduo = ''; 
    this.fecharModalCaminhoes();
    this.caminhaoid = caminhao.id!;
  }
  
  abrirModalPontosColeta() {
    this.filtroResiduo = this.rotaAtual.tipoResiduo
    this.modalPontosColetaVisivel = true;
  }

  fecharModalPontosColeta() {
    this.modalPontosColetaVisivel = false;
  }
  
  onPontoColetaSelecionado(ponto: PontoColeta): void {
    this.rotaAtual.destino = ponto;
    this.fecharModalPontosColeta();
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
      this.rotaService.atualizar(this.idEditando, this.rotaAtual).subscribe(callback);
    } else {
      this.rotaService.salvar(this.rotaAtual).subscribe(callback);
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
        error: (err: any) => {
          const msg = this.handleErrorMessage.handleError(err);
          this.mostrarMensagem('erro', msg);
        }
      });
    }
  }

  calcularRota(): void {
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
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    });
  }
}
