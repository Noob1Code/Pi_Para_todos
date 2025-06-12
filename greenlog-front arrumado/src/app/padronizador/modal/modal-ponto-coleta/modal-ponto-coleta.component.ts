import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { PontoColeta } from '../../../cadastrar/ponto-coleta/ponto-coleta.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PontoColetaService } from '../../../cadastrar/ponto-coleta/ponto-coleta.service';

@Component({
  selector: 'app-modal-ponto-coleta',
  standalone: true, 
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-ponto-coleta.component.html',
  styleUrl: './modal-ponto-coleta.component.css'
})
export class ModalPontoColetaComponent implements OnChanges {
  @Input() visivel: boolean = false;
  @Input() caminhoaid: number | undefined;
  @Input() residuo: string | undefined;
  @Output() fechado = new EventEmitter<void>();
  @Output() pontoColetaSelecionado = new EventEmitter<PontoColeta>();

  pontosColetaFiltrados: PontoColeta[] = [];
  pontosColetaCompativeis: PontoColeta[] = []
  termoPesquisa: string = '';
  carregando = false;
  
  constructor(private pontoColetaService: PontoColetaService) {}
  
  ngOnChanges(changes: SimpleChanges): void {
  if ((changes['caminhoaid'] || changes['residuo']) && this.caminhoaid !== undefined) {
    this.carregarPontosCompativeis(this.caminhoaid);
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

  carregarPontosCompativeis(caminhaoId: number) {
  this.carregando = true;

  this.pontoColetaService.getPontosDeColetaCompativeis(caminhaoId).subscribe({
    next: (pontos) => {
      const residuoAlvo = this.residuo?.toLowerCase() ?? '';

      const filtrados = residuoAlvo
        ? pontos.filter(p =>
            p.tiposResiduosAceitos.some(tipo => tipo.toLowerCase() === residuoAlvo)
          )
        : pontos;

      this.pontosColetaCompativeis = filtrados;
      this.pontosColetaFiltrados = [...filtrados].sort((a, b) => a.nome.localeCompare(b.nome));
      this.carregando = false;
    },
    error: () => {
      this.pontosColetaCompativeis = [];
      this.pontosColetaFiltrados = [];
      this.carregando = false;
    }
  });
}
}
