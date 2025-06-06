import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Bairros } from './cadastro.bairros.model';
import { CadastroBairrosService } from './cadastro.bairros.service';

@Component({
  selector: 'app-bairros',
  standalone: true,
  imports: [ CommonModule, FormsModule, ],
  templateUrl: './cadastro.bairros.component.html',
  styleUrl: './cadastro.bairros.component.css'
})

export class BairrosComponent implements OnInit {

  @Input() visivel = true;
  @Output() fechado = new EventEmitter<void>();
mostrarModalBairros: any;

fechar() {
  this.visivel = false;
  this.fechado.emit();
}

  bairros = '';
  idEditando: number | null = null;
  formEnviado = false;
  bairross: Bairros[] = [];

  constructor(private cadastroBairrosService: CadastroBairrosService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.cadastroBairrosService.getBairros().subscribe((data) => {
      this.bairross = data;
    });
  }

  salvar() {
      this.formEnviado = true;
  
      if (!this.bairros) {
        alert('Preencha todos os campos corretamente.');
        return;
      }
  
      const novobairros: Bairros = {nome: this.bairros};
  
      if (this.idEditando !== null) {
    this.cadastroBairrosService.atualizarBairros(this.idEditando,novobairros).subscribe(() => {
      alert('Bairros atualizada com sucesso!');
      this.limparCampos();
      this.listar();
    });
  } else {
    this.cadastroBairrosService.adicionarBairros(novobairros).subscribe(() => {
      alert('Bairros salva com sucesso!');
      this.limparCampos();
      this.listar();
    });
  }
  
    this.formEnviado = false;
  }
  
    editar(d: Bairros, id: number | undefined) {
      if (id === undefined) return;
      this.idEditando = id;
      this.bairros = d.nome;
    }
  
    excluir(id:  number | undefined) {
      if (id === undefined) return;
    if (confirm('Tem certeza que deseja excluir este Bairros?')) {
      this.cadastroBairrosService.excluirBairros(id).subscribe(() => {
        alert('Bairros excluída com sucesso!');
        this.listar();
      });
    }
    }
  
    limparCampos() {
      this.bairros = '';
      this.idEditando = null;
    }

abrir() {
  this.mostrarModalBairros = true;
}

}
