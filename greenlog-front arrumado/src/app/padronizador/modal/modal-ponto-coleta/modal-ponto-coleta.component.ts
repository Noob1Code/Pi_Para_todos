import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { PontoColeta } from '../../../cadastrar/ponto-coleta/ponto-coleta.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modal-ponto-coleta',
  standalone: true, 
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-ponto-coleta.component.html',
  styleUrl: './modal-ponto-coleta.component.css'
})
export class ModalPontoColetaComponent implements OnChanges {
  @Input() visivel: boolean = false;
  @Input() pontosColetaCompativeis: PontoColeta[] | null = null;
  @Output() fechado = new EventEmitter<void>();
  @Output() pontoColetaSelecionado = new EventEmitter<PontoColeta>();

  pontosColetaFiltrados: PontoColeta[] = [];
  termoPesquisa: string = '';
  carregando = false;
  
  constructor() {}
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['pontosColetaCompativeis']) {
      const novosPontos = changes['pontosColetaCompativeis'].currentValue;
      if (novosPontos) {
        this.pontosColetaFiltrados = [...novosPontos].sort((a, b) => a.nome.localeCompare(b.nome));
        this.carregando = false;
      } else {
        this.pontosColetaFiltrados = [];
        this.carregando = true;
      }
    }
  }

  filtrar(): void {
    const termo = this.termoPesquisa.trim().toLowerCase();
    const listaBase = this.pontosColetaCompativeis || [];

    if (!termo) {
        this.pontosColetaFiltrados = [...listaBase];
        return;
    }

    this.pontosColetaFiltrados = listaBase.filter(p =>
      p.nome.toLowerCase().includes(termo) ||
      p.bairro.nome.toLowerCase().includes(termo)
    );
  }
  
  selecionar(pontoColeta: PontoColeta) {
    this.pontoColetaSelecionado.emit(pontoColeta);
  }
  
  fechar(): void {
    this.fechado.emit();
  }
}
