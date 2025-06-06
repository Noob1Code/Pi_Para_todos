import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Rota } from './rotas.model';
import { RotasService } from './rotas.service';
import { TopbarComponent } from '../../padronizacao/menu/topbar/topbar.component';
import { SidebarComponent } from '../../padronizacao/menu/sidebar/sidebar.component';
import { ModalCaminhaoComponent } from '../../padronizacao/modal/modal-caminhao/modal-caminhao.component';
import { Caminhao } from '../caminhao/caminhao.service';
import { PontoColeta } from '../pontos-coleta/ponto-coleta.model';
import { ModalOrigemDestinoComponent } from "../../padronizacao/modal/modal-origem-destino/modal-origem-destino.component";

@Component({
  selector: 'app-rotas',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TopbarComponent,
    SidebarComponent,
    ModalCaminhaoComponent,
    ModalOrigemDestinoComponent
  ],
  templateUrl: './rotas.component.html',
  styleUrls: ['./rotas.component.css']
})
export class RotasComponent implements OnInit {

  selectedCaminhao: Caminhao | null = null;
  selectedOrigem: PontoColeta | null = null;
  selectedDestino: PontoColeta | null = null;


  caminhao = '';
  data: string = new Date().toISOString().slice(0, 10);
  origem = '';
  destino = '';
  idEditando: number | null = null;

  rotas: Rota[] = [];

  mostrarModalCaminhao = false;
  termoBuscaCaminhao = '';
  mostrarModalOrigem = false;
  termoBuscaOrigem = '';
  caminhoes: any[] = [];
  pontos: any[] = [];

  termoBuscaDestino = '';
  mostrarModalDestino = false;

  constructor(private rotasService: RotasService) { }

  // ================== Ciclo de vida ==================
  ngOnInit(): void {
    this.listar();
  }

  // ================== CRUD ==================
  listar(): void {
    this.rotasService.getRotas().subscribe((data) => {
      this.rotas = data;
    });
  }

  salvar(): void {
    if (!this.selectedCaminhao) {
      alert('Selecione um caminhão!');
      return;
    }
    if (!this.selectedOrigem) {
      alert('Selecione a origem!');
      return;
    }
    if (!this.selectedDestino) {
      alert('Selecione o destino!');
      return;
    }
    if (this.selectedOrigem.id === this.selectedDestino.id) {
      alert('Origem e destino não podem ser iguais!');
      return;
    }

    const rota = new Rota(
      this.selectedCaminhao,
      new Date(this.data).toISOString(),
      this.selectedOrigem,
      this.selectedDestino
    );

    if (this.idEditando !== null) {
      this.rotasService.atualizarRotas(this.idEditando, rota).subscribe(() => {
        alert('Rota atualizada com sucesso!');
        this.limparCampos();
        this.listar();
      });
    } else {
      this.rotasService.adicionarRotas(rota).subscribe(() => {
        alert('Rota salva com sucesso!');
        this.limparCampos();
        this.listar();
      });
    }
  }

  editar(rota: Rota, id: number | undefined): void {
  if (id === undefined) return;

  this.selectedCaminhao = rota.caminhao;
  this.selectedOrigem = rota.origem;
  this.selectedDestino = rota.destino;

  this.caminhao = `${rota.caminhao.placa} (${rota.caminhao.residuos})`;
  this.data = rota.data.slice(0, 10);
  this.origem = `${rota.origem.nome} (${rota.origem.tiposResiduosAceitos})`;
  this.destino = `${rota.destino.nome} (${rota.destino.tiposResiduosAceitos})`;

  this.idEditando = id;
}

  excluir(id: number | undefined): void {
    if (id === undefined) return;
    if (confirm('Tem certeza que deseja excluir esta Rota?')) {
      this.rotasService.excluirRotas(id).subscribe(() => {
        alert('Rota excluída com sucesso!');
        this.listar();
      });
    }
  }

  // ================== Utilitários ==================
  limparCampos(): void {
    this.caminhao = '';
    this.selectedCaminhao = null;
    this.data = new Date().toISOString().slice(0, 10);
    this.origem = '';
    this.selectedOrigem = null;
    this.destino = '';
    this.idEditando = null;
    this.fecharModalCaminhao();
    this.fecharModalOrigem();
    this.fecharModalDestino();
  }

  // =============== Modal helpers ===================
  abrirModalCaminhao(): void {
    this.mostrarModalCaminhao = true;
  }

  fecharModalCaminhao(): void {
    this.mostrarModalCaminhao = false;
  }

  selecionarCaminhao(c: Caminhao): void {
    this.selectedCaminhao = c;
    this.caminhao = `${c.placa} (${c.residuos})`; // apenas visual
    this.fecharModalCaminhao();
  }

  // =============== Simula busca de caminhões ============
  buscarCaminhoes(): void {
    // Exemplo de mock, substitua por chamada a serviço real se houver
    this.caminhoes = [];
  }


  abrirModalOrigem(): void {
    this.mostrarModalOrigem = true;
  }

  fecharModalOrigem(): void {
    this.mostrarModalOrigem = false;
  }

  selecionarOrigem(o: PontoColeta): void {
    this.selectedOrigem = o;
    this.origem = `${o.id} (${o.tiposResiduosAceitos})`; // atualiza 'origem', não 'ponto'
    this.fecharModalOrigem();
  }

  // =============== Simula busca de Pontos de Coleta ============
  buscarOrigem(): void {
    // Exemplo de mock, substitua por chamada a serviço real se houver
    this.pontos = [];
  }
  abrirModalDestino(): void {
    this.mostrarModalDestino = true;
  }

  fecharModalDestino(): void {
    this.mostrarModalDestino = false;
  }

  selecionarDestino(o: PontoColeta): void {
    this.selectedDestino = o;
    this.destino = `${o.id} (${o.tiposResiduosAceitos})`;
    this.fecharModalDestino();
  }
}
