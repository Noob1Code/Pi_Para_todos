import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Conexao } from './conexao.modulo';
import { ConexaoService } from './conexao.service';
import { TopbarComponent } from "../../padronizacao/menu/topbar/topbar.component";
import { SidebarComponent } from "../../padronizacao/menu/sidebar/sidebar.component";

@Component({
  selector: 'app-conexao',
  standalone: true,
  imports: [CommonModule, FormsModule, TopbarComponent, SidebarComponent],
  templateUrl: './conexao.component.html',
  styleUrl: './conexao.component.css'
})
export class ConexaoComponent implements OnInit{
  // ================== Variáveis ==================
  rua = "";
  origem = "";
  destino = "";
  quilometros = 0;
  idEditando: number | null = null
  conexoes : Conexao[] = [];

  constructor(private conexaoService : ConexaoService) {}
  
  ngOnInit(): void {
    this.listar();
  }
  
  listar() {
    this.conexaoService.getConexoes().subscribe((data) => {
    this.conexoes = data;
    });
  }

  salvar(): void {

    const conexao = new Conexao(this.rua, this.origem, this.destino, this.quilometros);
  
    if (this.idEditando !== null) {
      this.conexaoService.atualizarConexoes(this.idEditando, conexao).subscribe(() => {
        alert('Rua atualizada com sucesso!');
        this.limparCampos();
        this.listar();
      });
    } else {
      this.conexaoService.adicionarConexoes(conexao).subscribe(() => {
        alert('Rua salva com sucesso!');
        this.limparCampos();
        this.listar();
      });
    }
  }

  editar(connexao: Conexao, id: number | undefined): void {
    console.log(id);
    if (id === undefined) return;
    this.rua = connexao.rua;
    this.origem = connexao.origem;
    this.destino = connexao.destino;
    this.quilometros = connexao.quilometros;
    this.idEditando = id
  }

  excluir(id: number | undefined) {
    if (id === undefined) return;
    if (confirm('Tem certeza que deseja excluir esta Rua?')) {
      this.conexaoService.excluirConexoes(id).subscribe(() => {
        alert('Rua excluída com sucesso!');
        this.listar();
      });
    }
  }

  limparCampos(): void {
    this.rua = '';
    this.origem = '';
    this.destino = '';
    this.quilometros = 0;
    this.idEditando = null
  }
   
}
