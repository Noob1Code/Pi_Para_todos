import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Caminhao } from './caminhao.modal';
import { CaminhaoService } from './caminhao.service';

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

  constructor(private caminhaoService: CaminhaoService) {}

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
    this.inicializarCheckboxes();
    this.buscarTodos();
  }

  onTipoResiduoChange(tipo: string, isChecked: boolean): void {
  this.tiposResiduosSelecionados[tipo] = isChecked;

  this.caminhaoAtual.residuos = this.opcoesTiposResiduos
    .filter(t => this.tiposResiduosSelecionados[t]);
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
    error: () => (this.mostrarMensagem('erro','Erro ao buscar caminhões.')),
  });
}

  salvar(): void {
    
    const algumSelecionado = Object.values(this.tiposResiduosSelecionados).some(v => v === true);

    if (!algumSelecionado) {
    this.erroResiduos = true;
    return;
  }

  this.erroResiduos = false;

    if (this.idEditando) {
      this.caminhaoService.atualizar(this.idEditando, this.caminhaoAtual).subscribe({
        next: () => {
          this.mostrarMensagem('editado');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro','Erro ao atualizar caminhao.')),
      });
    } else {
      console.log('Enviando caminhão:', this.caminhaoAtual);
      this.caminhaoService.salvar(this.caminhaoAtual).subscribe({
        next: () => {
          this.mostrarMensagem('salvo');
          this.resetForm();
          this.buscarTodos();
        },
        error: () => (this.mostrarMensagem('erro','Erro ao cadastrar caminhao.')),
      });
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
        error: () => (this.mostrarMensagem('erro','Erro ao excluir caminhao.')),
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

  this.caminhaoAtual.residuos = this.opcoesTiposResiduos
    .filter(t => this.tiposResiduosSelecionados[t]);
  }

  resetForm(): void {
    this.caminhaoAtual = {
      placa: '',
      motorista: '',
      capacidade: null,
      residuos: []
    };
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