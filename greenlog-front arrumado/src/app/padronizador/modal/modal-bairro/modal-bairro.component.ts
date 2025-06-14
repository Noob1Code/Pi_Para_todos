import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
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
  @Input() excluirBairroId?: number;
  @Input() filtro: string | undefined;
  @ViewChild('inputPesquisa') inputPesquisa: ElementRef | undefined;

  bairros: Bairro[] = [];
  termoPesquisa: string = '';
  bairrosFiltrados: Bairro[] = [];
  carregando = true;

  constructor(private bairroService: BairroService) {}

  ngOnInit() {
    this.carregar();
    this.bairrosFiltrados = [...this.bairros];
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
  ngAfterViewInit() {
  if (this.visivel) {
    setTimeout(() => this.inputPesquisa!.nativeElement.focus(), 0);
  }
}

isBairroDesabilitado(bairro : Bairro): boolean {
  return bairro.id === this.excluirBairroId || bairro.nome === this.filtro;
}
}