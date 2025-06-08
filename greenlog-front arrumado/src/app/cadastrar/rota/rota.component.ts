import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Rota } from './rota.model';
import { RotaService } from './rota.service';

@Component({
  selector: 'app-rota',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './rota.component.html',
  styleUrl: './rota.component.css'
})
export class RotaComponent implements OnInit{
  
  rotas: Rota [] = [];
  rotaAtual: Rota = {
      caminhao: { placa: '', motorista: '', capacidade: 0, residuos: []}, 
      destino:{
        nome: '',
        responsavel: '',
        telefoneResponsavel: '',
        emailResponsavel: '',
        endereco: '',
        horarioFuncionamento: '',
        bairro: { id: 0, nome: '' },
        tiposResiduosAceitos: []
      }, 
      origem:{
        nome: '',
        responsavel: '',
        telefoneResponsavel: '',
        emailResponsavel: '',
        endereco: '',
        horarioFuncionamento: '',
        bairro: { id: 0, nome: '' },
        tiposResiduosAceitos: []
      }, 
      tipoResiduo: ''
    };

  idEditando: number | null = null;
  
  mensagemSalvo = false;
  mensagemEditado = false;
  mensagemExcluido = false;
  mensagemErro = '';
  
  constructor(private rotaService: RotaService) {}

  ngOnInit(): void {
    this.buscarTodos();
  }

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

  buscarTodos(): void {
    this.rotaService.listar().subscribe({
      next: (res) => (this.rotas = res),
      error: () => (this.mensagemErro = 'Erro ao buscar ruas.'),
    });
  }

  salvar(): void {
    this.limparMensagens();
  
    if (this.idEditando) {
      this.rotaService.atualizar(this.idEditando, this.rotaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao atualizar rua.'),
      });
    } else {
      this.rotaService.salvar(this.rotaAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
      error: () => (this.mensagemErro = 'Erro ao cadastrar rua.'),
      });
    }
  }
  
  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este rua?');
    if (confirmar) {
      this.rotaService.excluir(id).subscribe({
        next: () => {
          this.mostrarMensagem('excluido');
          this.buscarTodos();
        },
        error: () => (this.mensagemErro = 'Erro ao excluir rua.'),
      });
    }
  }
  
  editar(rota: Rota): void {
    this.idEditando = rota.id ?? null;
    this.rotaAtual = { ...rota };
    this.limparMensagens();
  }
  
  resetForm(): void {
    this.rotaAtual = {
      caminhao: { placa: '', motorista: '', capacidade: 0, residuos: []}, 
      destino:{
        nome: '',
        responsavel: '',
        telefoneResponsavel: '',
        emailResponsavel: '',
        endereco: '',
        horarioFuncionamento: '',
        bairro: { id: 0, nome: '' },
        tiposResiduosAceitos: []
      }, 
      origem:{
        nome: '',
        responsavel: '',
        telefoneResponsavel: '',
        emailResponsavel: '',
        endereco: '',
        horarioFuncionamento: '',
        bairro: { id: 0, nome: '' },
        tiposResiduosAceitos: []
      },  
      tipoResiduo: ''
    };
    this.idEditando = null;
    this.limparMensagens();
  }
  
  limparMensagens(): void {
    this.mensagemErro = '';
  }
  
  validarCampos(rotaAtual: Rota): boolean {
    
    if (!rotaAtual.caminhao || rotaAtual.caminhao === null) {
      this.mensagemErro = 'O caminhao é obrigatório.';
      return false;
    }

    if (!rotaAtual.origem || rotaAtual.origem === null) {
      this.mensagemErro = 'A origem é obrigatório.';
      return false;
    }

    if (!rotaAtual.destino || rotaAtual.destino === null) {
      this.mensagemErro = 'O destino é obrigatório.';
      return false;
    }

    if (!rotaAtual.tipoResiduo || rotaAtual.tipoResiduo.trim() === '') {
      this.mensagemErro = 'O tipo de residuo é obrigatória.';
      return false;
    }

    return true;
  }

}
