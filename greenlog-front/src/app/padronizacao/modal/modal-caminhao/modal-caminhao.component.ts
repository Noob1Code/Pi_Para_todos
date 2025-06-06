import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CaminhaoService, Caminhao } from '../../../cadastros/caminhao/caminhao.service'; // ajuste o caminho

@Component({
  selector: 'app-modal-caminhao',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './modal-caminhao.component.html',
  styleUrls: ['./modal-caminhao.component.css']
})
export class ModalCaminhaoComponent implements OnChanges {
  @Input() mostrarCaminhao: boolean = false;
  @Input() termoBuscaCaminhao: string = '';
  @Output() termoBuscaCaminhaoChange = new EventEmitter<string>();

  @Output() selecionar = new EventEmitter<Caminhao>();
  @Output() fechar = new EventEmitter<void>();

  caminhoes: Caminhao[] = [];
  caminhoesFiltrados: Caminhao[] = [];
  

  constructor(private caminhaoService: CaminhaoService) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['mostrarCaminhao'] && this.mostrarCaminhao) {
      this.buscarCaminhoes();
    }
  }

  buscarCaminhoes() {
    this.caminhaoService.listar().subscribe((data) => {
      this.caminhoes = data;
      this.aplicarFiltro();
    });
  }

  onTermoBuscaChange(valor: string) {
    this.termoBuscaCaminhao = valor;
    this.termoBuscaCaminhaoChange.emit(valor);
    this.aplicarFiltro();
  }

  aplicarFiltro() {
    const termo = this.termoBuscaCaminhao.toLowerCase();
    this.caminhoesFiltrados = this.caminhoes.filter(
      c =>
        c.placa.toLowerCase().includes(termo) ||
        c.id.toString().includes(termo) ||
        c.residuos.toLowerCase().includes(termo)
    );
  }

  onSelecionar(c: Caminhao) {
    this.selecionar.emit(c);
  }

  onFechar() {
    this.fechar.emit();
  }
}
