import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PontoColeta } from '../../../cadastrar/ponto-coleta/ponto-coleta.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PontoColetaService } from '../../../cadastrar/ponto-coleta/ponto-coleta.service';

@Component({
  selector: 'app-modal-ponto-coleta',
  standalone: true, 
  imports: [CommonModule,FormsModule],
  templateUrl: './modal-ponto-coleta.component.html',
  styleUrl: './modal-ponto-coleta.component.css'
})
export class ModalPontoColetaComponent implements OnInit {
  @Input() visivel: boolean = false;
  @Output() fechado = new EventEmitter<void>();
  @Output() pontoColetaSelecionado = new EventEmitter<PontoColeta>();

  pontosColeta: PontoColeta[] = [];
  termoPesquisa: string = '';
  pontosColetaFiltrados: PontoColeta[] = [];
  carregando = true;
  
  constructor(private pontoColetaService: PontoColetaService) {}
  
  ngOnInit() {
    this.carregar();
    this.pontosColetaFiltrados = [...this.pontosColeta];
  }
  
  filtrar(): void {
    const termo = this.termoPesquisa.trim().toLowerCase();

    this.pontosColetaFiltrados = this.pontosColeta.filter(c =>
      c.nome.toLowerCase().includes(termo) ||
      c.tiposResiduosAceitos.some(r => r.toLowerCase().includes(termo))
    );
  }
  
  carregar(): void {
    this.carregando = true;
    this.pontoColetaService.listar().subscribe({
      next: (data) => {
        this.pontosColeta = data.sort((a, b) => a.nome.localeCompare(b.nome));
        this.pontosColetaFiltrados = [...data];
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar pontosColeta', err);
        alert('Não foi possível carregar a lista de pontosColeta.');
        this.carregando = false;
      }
    });
  }
  
  selecionar(pontoColeta: PontoColeta) {
    this.pontoColetaSelecionado.emit(pontoColeta);
  }
  
  fechar(): void {
    this.fechado.emit();
  }
}
