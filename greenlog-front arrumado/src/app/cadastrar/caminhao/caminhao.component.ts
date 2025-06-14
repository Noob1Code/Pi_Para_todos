import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Caminhao } from './caminhao.modal';
import { CaminhaoService } from './caminhao.service';
import { HandleErrorMessageService } from '../../handle-error-message.service';

@Component({
  selector: 'app-caminhao',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './caminhao.component.html',
  styleUrl: './caminhao.component.css'
})
export class CaminhaoComponent implements OnInit{

  caminhoes: Caminhao[] = [];
  caminhaoAtual: Caminhao = { placa: '', motorista: '', capacidade: null, residuos: []};
  idEditando: number | null = null;
  tiposResiduosSelecionados: { [key: string]: boolean } = {};
  opcoesTiposResiduos: string[] = ['Plástico', 'Papel', 'Metal', 'Orgânico'];
  erroResiduos = false;
  mensagem: { tipo: 'salvo' | 'editado' | 'excluido' | 'erro' | null; texto: string } = { tipo: null, texto: '' };

  constructor(
    private caminhaoService: CaminhaoService,
    private handleErrorMessage: HandleErrorMessageService,
  ) {}

  mostrarMensagem(tipo: 'salvo' | 'editado' | 'excluido' | 'erro', textoPersonalizado?: string): void {
    const textos = {
      salvo: ' Caminhão, cadastrado com sucesso!',
      editado: ' Caminhão, atualizado com sucesso!',
      excluido: ' Caminhão, excluído com sucesso!',
      erro: textoPersonalizado || '❌ Ocorreu um erro ao processar a solicitação.'
    };
    this.mensagem = { tipo, texto: textos[tipo] };
    if (tipo !== 'erro') {
      setTimeout(() => {
        this.mensagem = { tipo: null, texto: '' };
      }, 6000);
    }
  }

  ngOnInit(): void {
    this.inicializarCheckboxes();
    this.buscarTodos();
  }

  onTipoResiduoChange(tipo: string, isChecked: boolean): void {
    this.tiposResiduosSelecionados[tipo] = isChecked;
    this.caminhaoAtual.residuos = this.opcoesTiposResiduos.filter(t => this.tiposResiduosSelecionados[t]);
  }

  buscarTodos(): void {
    this.caminhaoService.listar().subscribe({
      next: (res) => {
        this.caminhoes = res.map((caminhao) => ({
          ...caminhao,
          residuos: Array.isArray(caminhao.residuos)
            ? caminhao.residuos
            : (caminhao.residuos ? [caminhao.residuos] : [])
        }));
      },
      error: (err: any) => {
        const msg = this.handleErrorMessage.handleError(err);
        this.mostrarMensagem('erro', msg);
      }
    });
  }

  salvar(form: NgForm): void {
    
    const algumSelecionado = Object.values(this.tiposResiduosSelecionados).some(v => v === true);
    if (!algumSelecionado) {
      this.erroResiduos = true;
      return;
    }
    this.erroResiduos = false;

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
      this.caminhaoService.atualizar(this.idEditando, this.caminhaoAtual).subscribe(callback);
    } else {
      this.caminhaoService.salvar(this.caminhaoAtual).subscribe(callback);
    }
  }

  excluir(id: number): void {
    const confirmar = confirm('Tem certeza que deseja excluir este caminhao?');
    if (confirmar) {
      this.caminhaoService.excluir(id).subscribe({
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

  editar(caminhao: Caminhao): void {
    this.idEditando = caminhao.id ?? null;
    this.caminhaoAtual = { ...caminhao };
  
    this.tiposResiduosSelecionados = {};

    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = caminhao.residuos.includes(tipo);
    });

    this.caminhaoAtual.residuos = this.opcoesTiposResiduos.filter(t => this.tiposResiduosSelecionados[t]);
  }

  resetForm(form?: NgForm): void {
    this.caminhaoAtual = {
      placa: '',
      motorista: '',
      capacidade: null,
      residuos: []
    };
    if (form) {
      form.resetForm();
    }
    this.idEditando = null;
    this.inicializarCheckboxes();
  }

  inicializarCheckboxes(): void {
    this.opcoesTiposResiduos.forEach(tipo => {
      this.tiposResiduosSelecionados[tipo] = false;
    });
  }

  nenhumResiduoSelecionado(): boolean {
    return !Object.values(this.tiposResiduosSelecionados).some(v => v === true);
  }
}