import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { PontoColetaService, PontoColeta } from '../../../cadastros/pontos-coleta/ponto-coleta.service';


@Component({
  selector: 'app-modal-origem-destino',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-origem-destino.component.html',
  styleUrl: './modal-origem-destino.component.css'
})
export class ModalOrigemDestinoComponent implements OnChanges {
  @Input() mostrarOrigemDestino: boolean = false;
  @Input() termoBuscaOrigemDestino: string = '';
  @Output() termoBuscaOrigemDestinoChange = new EventEmitter<string>();

  @Output() selecionar = new EventEmitter<PontoColeta>();
  @Output() fechar = new EventEmitter<void>();
  @Input() origemSelecionadaId: number | null = null;

  pontosColeta: PontoColeta[] = [];
  origensDestinosFiltadros: PontoColeta[] = [];


  constructor(private pontoColetaService: PontoColetaService) { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['mostrarOrigemDestino'] && this.mostrarOrigemDestino) {
      this.buscarOrigensDestinos();
    }
  }

  buscarOrigensDestinos() {
    this.pontoColetaService.getPontosColeta().subscribe((data) => {
      this. pontosColeta = data;
      this.aplicarFiltro();
    });
  }


  onTermoBuscaChange(valor: string) {
    this.termoBuscaOrigemDestino = valor;
    this.termoBuscaOrigemDestinoChange.emit(valor);
    this.aplicarFiltro();
  }

  aplicarFiltro(): void {
  const termo = this.termoBuscaOrigemDestino.toLowerCase();
  this.origensDestinosFiltadros = this.pontosColeta.filter(o =>
    (this.origemSelecionadaId == null || o.id !== this.origemSelecionadaId) &&
    (
      o.id?.toString().includes(termo) ||
      o.nome.toLowerCase().includes(termo) ||
      o.tiposResiduosAceitos.join(', ').toLowerCase().includes(termo)
    )
  );
}

  onSelecionar(o: PontoColeta) {
    this.selecionar.emit(o);
  }

  onFechar() {
    this.fechar.emit();
  }

}
