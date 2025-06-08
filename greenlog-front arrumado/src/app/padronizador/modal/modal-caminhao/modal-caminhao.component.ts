import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Caminhao } from '../../../cadastrar/caminhao/caminhao.modal';
import { CaminhaoService } from '../../../cadastrar/caminhao/caminhao.service';

@Component({
  selector: 'app-modal-caminhao',
  standalone: true, 
  imports: [CommonModule,FormsModule],
  templateUrl: './modal-caminhao.component.html',
  styleUrl: './modal-caminhao.component.css'
})
export class ModalCaminhaoComponent implements OnInit {
  @Input() visivel: boolean = false;
  @Output() fechado = new EventEmitter<void>();
  @Output() caminhaoSelecionado = new EventEmitter<Caminhao>();

  caminhoes: Caminhao[] = [];
  termoPesquisa: string = '';
  caminhoesFiltrados: Caminhao[] = [];
  carregando = true;
  
  constructor(private caminhaoService: CaminhaoService) {}
  
  ngOnInit() {
    this.carregar();
    this.caminhoesFiltrados = [...this.caminhoes]; // inicia com todos
  }
  
  filtrar(): void {
    const termo = this.termoPesquisa.trim().toLowerCase();

    this.caminhoesFiltrados = this.caminhoes.filter(c =>
      c.placa.toLowerCase().includes(termo) ||
      c.residuos.some(r => r.toLowerCase().includes(termo))
    );
  }
  
  carregar(): void {
    this.carregando = true;
    this.caminhaoService.listar().subscribe({
      next: (data) => {
        this.caminhoes = data.sort((a, b) => a.placa.localeCompare(b.placa));
        this.caminhoesFiltrados = [...data];
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar caminhoes', err);
        alert('Não foi possível carregar a lista de caminhoes.');
        this.carregando = false;
      }
    });
  }
  
  selecionar(caminhao: Caminhao) {
    this.caminhaoSelecionado.emit(caminhao);
  }
  
  fechar(): void {
    this.fechado.emit();
  }
}
