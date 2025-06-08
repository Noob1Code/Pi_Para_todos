import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Bairro } from '../../../cadastrar/bairro/bairro.model';
import { BairroService } from '../../../cadastrar/bairro/bairro.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modal-bairros',
  standalone: true, 
  imports: [CommonModule,FormsModule], 
  templateUrl: './modal-bairro.component.html',
})
export class ModalBairrosComponent implements OnInit {

  @Input() visivel: boolean = false;
  @Output() fechado = new EventEmitter<void>();
  @Output() bairroSelecionado = new EventEmitter<Bairro>();

  bairros: Bairro[] = [];
  termoPesquisa: string = '';
  bairrosFiltrados: Bairro[] = [];
  carregando = true;

  constructor(private bairroService: BairroService) {}

  ngOnInit() {
    this.carregar();
    this.bairrosFiltrados = [...this.bairros]; // inicia com todos
  }

  filtrar(): void {
    const termo = this.termoPesquisa.trim().toLowerCase();
    this.bairrosFiltrados = this.bairros.filter(b =>
      b.nome.toLowerCase().includes(termo)
    );
  }

  carregar(): void {
    this.carregando = true;
    this.bairroService.listar().subscribe({
      next: (data) => {
        this.bairros = data.sort((a, b) => a.nome.localeCompare(b.nome));
        this.bairrosFiltrados = [...data];
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar bairros', err);
        alert('Não foi possível carregar a lista de bairros.');
        this.carregando = false;
      }
    });
  }

  selecionar(bairro: Bairro) {
    this.bairroSelecionado.emit(bairro);
  }

  fechar(): void {
    this.fechado.emit();
  }
}