import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Rota } from './rota.model';
import { RotaService } from './rota.service';
import { ModalBairrosComponent } from "../../padronizador/modal/modal-bairro/modal-bairro.component";
import { ModalCaminhaoComponent } from "../../padronizador/modal/modal-caminhao/modal-caminhao.component";
import { Caminhao } from '../caminhao/caminhao.modal';
import { Bairro } from '../bairro/bairro.model';

@Component({
  selector: 'app-rota',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalBairrosComponent, ModalCaminhaoComponent],
  templateUrl: './rota.component.html',
  styleUrl: './rota.component.css'
})
export class RotaComponent implements OnInit{
  modalCaminhoesVisivel = false;
  modalBairrosVisivel = false;
  rotas: Rota[] = [];
  rotaAtual: Rota = {
    caminhao: null,
    destino: null,
    tipoResiduo: '',
    bairrosPercorridos: [],
    distanciaTotal:0
  }
  idEditando: number | null = null;

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
        tipo === 'salvo' ? 'Rota cadastrado com sucesso!' :
        tipo === 'editado' ? 'Rota atualizado com sucesso!' :
        tipo === 'excluido' ? 'Rota excluído com sucesso!' :
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
    this.rotaAtual = {
      caminhao: null,
      destino: null,
      tipoResiduo: '',
      bairrosPercorridos: [],
      distanciaTotal:0
    };
    this.idEditando = null;
  }
  editar(rota: Rota): void {
    this.idEditando = rota.id ?? null;
    this.rotaAtual = { ...rota };
  }

  abrirModalBairros(){
    this.modalBairrosVisivel = true;
    this.fecharModalCaminhoes();
  }

  fecharModalBairros(){
    this.modalBairrosVisivel = false;
  }
  
  onBairroSelecionado(event: Bairro): void {
    this.rotaAtual.destino = event;
    this.modalBairrosVisivel = false;
  }

  abrirModalCaminhoes(){
    this.modalCaminhoesVisivel = true;
    this.fecharModalBairros();
  }

  fecharModalCaminhoes(){
    this.modalCaminhoesVisivel = false;
  }

  onCaminhaoSelecionado(caminhao : Caminhao){
    this.rotaAtual.caminhao = caminhao;
    this.modalCaminhoesVisivel = false;
  }

  salvar(form: NgForm): void {

  if (this.idEditando) {
    this.rotaService.atualizar(this.idEditando, this.rotaAtual).subscribe({
      next: () => {
        this.mostrarMensagem('editado');
        this.resetForm(form);
        this.buscarTodos();
      },
      error: () => (this.mostrarMensagem('erro','Erro ao atualizar bairro.')),
    });
  } else {
    this.rotaService.salvar(this.rotaAtual).subscribe({
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
    const confirmar = confirm('Tem certeza que deseja excluir este ponto de coleta?');
    if (confirmar) {
      this.rotaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => this.mostrarMensagem('erro', 'Erro ao excluir ponto de coleta.'),
      });
    }
  }
}
