import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Rota } from '../../../cadastrar/rota/rota.model';
import { RotaService } from '../../../cadastrar/rota/rota.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-modal-rota',
  imports: [CommonModule,FormsModule],
  templateUrl: './modal-rota.component.html',
  styleUrl: './modal-rota.component.css'
})
export class ModalRotaComponent {
  @Input() visivel: boolean = false;
  @Output() fechado = new EventEmitter<void>();
  @Output() rotaSelecionado = new EventEmitter<Rota>();

  rotas: Rota[] = [];
  termoPesquisa: string = '';
  rotasFiltrados: Rota[] = [];
  carregando = true;
  
  constructor(private rotaService: RotaService) {}
  
  ngOnInit() {
    this.carregar();
    this.rotasFiltrados = [...this.rotas]; // inicia com todos
  }
  
  filtrar(): void {
    const termo = this.termoPesquisa.trim().toLowerCase();

    this.rotasFiltrados = this.rotas.filter(r =>
      r.destino?.nome.toLowerCase().includes(termo) ||
      r.caminhao?.placa.toLowerCase().includes(termo) ||
      r.bairrosPercorridos.some(r => r.toLowerCase().includes(termo))
    );
  }
  
  carregar(): void {
    this.carregando = true;
    this.rotaService.listar().subscribe({
      next: (data) => {
        this.rotas = data.sort((a, b) => a.destino!.nome.localeCompare(b.destino!.nome));
        this.rotasFiltrados = [...data];
        this.carregando = false;
      },
      error: (err) => {
        this.carregando = false;
      }
    });
  }
  
  selecionar(rota: Rota) {
    this.rotaSelecionado.emit(rota);
  }
  
  fechar(): void {
    this.fechado.emit();
  }
}
