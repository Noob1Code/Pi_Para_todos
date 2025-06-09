import { Component, Input, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { Usuario } from './cadastro.model';
import { CadastroUsuarioService } from './cadastro.service';
import { Router } from '@angular/router';
import { TopbarComponent } from "../../padronizador/menu/topbar/topbar.component";
import { SidebarComponent } from "../../padronizador/menu/sidebar/sidebar.component";

@Component({
  selector: 'app-cadastro-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxMaskDirective, TopbarComponent, SidebarComponent],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroUsuarioComponent implements OnInit{
  usuario: Usuario = {
    username: '',
    nome: '',
    cpf: '',
    idade: null,
    telefone: '',
    genero: '',
    senha: ''
  };

  usuarioLogado = false;

  @Input() logado: boolean = false;

  cpfMask = '000.000.000-00';
  telefoneMask = '(00) 00000-0000';
  opcoesGenero = ['Homem', 'Mulher', 'Outro', 'Prefiro não dizer'];

  mensagemSucesso = '';
  mensagemErro = '';
  errosValidacao: any = {};

  constructor(
    private cadastroService: CadastroUsuarioService,
    private router: Router
  ) {}

  ngOnInit() {
    if (typeof window !== 'undefined' && window.localStorage) {
      const usuarioStr = localStorage.getItem('usuarioLogado');
      if (usuarioStr) {
      const usuarioSalvo = JSON.parse(usuarioStr);
      const id = usuarioSalvo.id;

      this.cadastroService.buscarPorId(id).subscribe({
        next: (usuarioCompleto) => {
          this.usuario = usuarioCompleto;
          this.usuarioLogado = true;
        },
        error: (err) => {
          console.error('Erro ao buscar usuário por ID:', err);
        }
      });
    }
  }
  }

  salvar(form: NgForm): void {
    this.limparMensagens();

    if (
      !this.usuario.username ||
      !this.usuario.nome ||
      !this.usuario.cpf ||
      this.usuario.idade === null || this.usuario.idade <= 0 ||
      !this.usuario.telefone ||
      !this.usuario.genero ||
      !this.usuario.senha
    ) {
      this.mensagemErro = 'Todos os campos são obrigatórios, e a idade deve ser válida.';
      return;
    }

    this.cadastroService.cadastrar(this.usuario).subscribe({
      next: (res) => {
        this.mensagemSucesso = `Usuário "${res.nome}" cadastrado com sucesso! ✅`;
        this.resetForm(form);
      },
      error: (err) => {
        if (err.status === 400 && err.error) {
          this.errosValidacao = err.error;
          this.mensagemErro = 'Por favor, corrija os erros no formulário.';
        } else if (err.status === 409 && err.error?.erro) {
          this.mensagemErro = err.error.erro;
        } else if (err.status === 500 && err.error?.erro) {
          this.mensagemErro = err.error.erro;
        } else {
          this.mensagemErro = 'Erro ao tentar cadastrar usuário. Tente novamente.';
        }
      }
    });
  }

  resetForm(form?: NgForm): void {
    this.usuario = {
      username: '',
      nome: '',
      cpf: '',
      idade: null,
      telefone: '',
      genero: '',
      senha: ''
    };
    if (form) {
      form.resetForm();
    }
  }

  limparMensagens(): void {
    this.mensagemErro = '';
    this.mensagemSucesso = '';
    this.errosValidacao = {};
  }

  permitirSomenteLetras(event: KeyboardEvent): void {
    const regex = /^[A-Za-zÀ-ÿ\s]$/;
    if (!regex.test(event.key)) {
      event.preventDefault();
    }
  }

  voltar(){
    this.router.navigate(['/login']);
  }
}
