import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CaminhaoService, Caminhao, NovoCaminhao } from './caminhao.service';
import { TopbarComponent } from "../../padronizacao/menu/topbar/topbar.component";
import { SidebarComponent } from "../../padronizacao/menu/sidebar/sidebar.component";

@Component({
  selector: 'app-caminhao',
  standalone: true,
  imports: [CommonModule, FormsModule, TopbarComponent, SidebarComponent],
  templateUrl: './caminhao.component.html',
  styleUrls: ['./caminhao.component.css']
})
export class CaminhaoComponent implements OnInit {
  placa = '';
  motorista = '';
  capacidade: number | null = null;
  residuoSelecionado = '';

  caminhoes: Caminhao[] = [];
  idEditando: number | null = null;

  formEnviado = false;

  constructor(private caminhaoService: CaminhaoService) {}

  ngOnInit() {
    this.carregarCaminhoes();
  }

  carregarCaminhoes() {
    this.caminhaoService.listar().subscribe(data => this.caminhoes = data);
  }

  salvar() {
    this.formEnviado = true;

    if (!this.placa || !this.motorista || !this.capacidade || this.capacidade <= 0 || !this.residuoSelecionado) {
      alert('Preencha todos os campos corretamente.');
      return;
    }

    const caminhaoNovo: NovoCaminhao = {
      placa: this.placa,
      motorista: this.motorista,
      capacidade: this.capacidade!,
      residuos: this.residuoSelecionado // Agora é string
    };

    if (this.idEditando !== null) {
      const caminhaoAtualizado: Caminhao = {
        id: this.idEditando,
        ...caminhaoNovo
      };
      this.caminhaoService.atualizar(this.idEditando, caminhaoAtualizado).subscribe(() => {
        this.carregarCaminhoes();
        this.limparCampos();
      });
    } else {
      this.caminhaoService.salvar(caminhaoNovo).subscribe({
        next: () => {
          this.carregarCaminhoes();
          this.limparCampos();
        },
        error: err => {
          alert(err.error.message || 'Erro ao salvar caminhão');
        }
      });
    }

    this.formEnviado = false;
  }

  editar(c: Caminhao) {
    this.idEditando = c.id;
    this.placa = c.placa;
    this.motorista = c.motorista;
    this.capacidade = c.capacidade;
    this.residuoSelecionado = c.residuos; // Corrigido para string
  }

  excluir(id: number) {
    this.caminhaoService.excluir(id).subscribe(() => {
      this.carregarCaminhoes();
      if (this.idEditando === id) {
        this.limparCampos();
      }
    });
  }

  limparCampos() {
    this.placa = '';
    this.motorista = '';
    this.capacidade = null;
    this.residuoSelecionado = '';
    this.idEditando = null;
  }
  permitirSomenteLetras(event: KeyboardEvent) {
  const regex = /^[A-Za-zÀ-ÿ\s]$/;
  const inputChar = event.key;

  if (!regex.test(inputChar)) {
    event.preventDefault();
  }
}
}