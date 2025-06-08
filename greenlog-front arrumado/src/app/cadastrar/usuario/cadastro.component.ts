import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Necessário para ngModel, #cadastroForm, etc.
import { CommonModule } from '@angular/common'; // Necessário para *ngIf, *ngFor, etc.
import { NgxMaskDirective, NgxMaskPipe } from 'ngx-mask'; // Usado no seu HTML
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

// Interface para a resposta do backend (baseado em UsuarioResponseDTO)
interface UsuarioResponse {
  id: number;
  username: string;
  nome: string;
  cpf: string;
}

@Component({
  selector: 'app-cadastro-usuario',
  standalone: true,
  imports: [
    FormsModule, // Importado para [(ngModel)], #variavelTemplate="ngModel" e (ngSubmit)
    CommonModule, // Importado para *ngIf, *ngFor
    NgxMaskDirective, // Importado pois [mask] é usado no template
    // HttpClientModule é normalmente provido via provideHttpClient() em app.config.ts
  ],
  templateUrl: './cadastro.component.html', // Seu arquivo HTML
  styleUrls: ['./cadastro.component.css']
})
export class CadastroUsuarioComponent {
  // Este objeto 'usuario' deve corresponder aos campos no seu template HTML e no DTO do backend
  usuario = {
    username: '', // ✅ ADICIONADO - O backend exige. Adicione este campo ao seu HTML.
    nome: '',
    cpf: '',
    idade: null as number | null,
    telefone: '',
    genero: '', // O valor inicial será o 'disabled selected' do HTML
    senha: ''
  };

  // Máscaras usadas no seu HTML
  cpfMask = '000.000.000-00'; // Definido no HTML como [mask]="'000.000.000-00'"
  telefoneMask = '(00) 00000-0000'; // Definido no HTML como [mask]="'(00) 00000-0000'"

  // Usado no seu HTML para o <select> de gênero
  opcoesGenero: string[] = ['Homem', 'Mulher', 'Outro', 'Prefiro não dizer'];

  // Para mensagens de feedback da API, usadas no seu HTML
  mensagemErro: string = '';
  mensagemSucesso: string = '';

  // Para armazenar erros de validação específicos de campos vindos do backend
  // Você pode usar isso para exibir erros mais detalhados no HTML se desejar.
  errosValidacao: any = {};

  // URL do endpoint de cadastro do backend
  private apiUrl = 'http://localhost:8080/api/usuarios/cadastrar';

  constructor(private http: HttpClient) {}

  onSubmit() {
    this.mensagemErro = '';
    this.mensagemSucesso = '';
    this.errosValidacao = {};

    // Validação básica no frontend (o backend fará a validação completa)
    // O botão de submit no seu HTML já está desabilitado com [disabled]="cadastroForm.invalid"
    // Esta verificação extra no TS pode ser mantida como uma camada adicional,
    // especialmente para o campo 'username' que você precisará adicionar ao HTML.
    if (
      !this.usuario.username || // Garanta que o username (a ser adicionado no HTML) foi preenchido
      !this.usuario.nome ||
      !this.usuario.cpf ||
      this.usuario.idade === null || this.usuario.idade <= 0 ||
      !this.usuario.telefone ||
      !this.usuario.genero ||
      !this.usuario.senha
    ) {
      this.mensagemErro = 'Todos os campos são obrigatórios, incluindo o username, e a idade deve ser válida.';
      // console.error('Formulário inválido na submissão (verificar campos):', this.usuario);
      return;
    }

    // Prepara os dados para enviar ao backend, removendo máscaras de CPF e telefone
    const dadosParaBackend = {
      ...this.usuario,
      cpf: this.usuario.cpf.replace(/\D/g, ''), // Remove não dígitos
      telefone: this.usuario.telefone.replace(/\D/g, '') // Remove não dígitos
    };

    this.http.post<UsuarioResponse>(this.apiUrl, dadosParaBackend).subscribe({
      next: (response) => {
        this.mensagemSucesso = `Usuário "${response.nome}" (Login: ${response.username}) cadastrado com sucesso! ✅`;
        // console.log('Cadastro bem-sucedido:', response);

        // Limpa o formulário após o sucesso
        this.usuario = {
          username: '',
          nome: '',
          cpf: '',
          idade: null,
          telefone: '',
          genero: '',
          senha: ''
        };
        // Você pode querer resetar o estado do formulário também, se estiver usando #cadastroForm="ngForm" para mais controle
        // Ex: cadastroForm.resetForm(); // Isso requer passar o #cadastroForm para o método onSubmit.
      },
      error: (err: HttpErrorResponse) => {
        // console.error('Erro no cadastro:', err);
        if (err.status === 400 && err.error) {
          // Erros de validação do DTO do backend
          this.errosValidacao = err.error;
          this.mensagemErro = 'Por favor, corrija os erros indicados no formulário. 🔍';
          // Se você quiser mostrar os erros específicos no HTML, pode iterar sobre this.errosValidacao
          // ou acessar campos específicos como this.errosValidacao.cpf
        } else if (err.status === 409 && err.error?.erro) {
          // Conflito (username/CPF duplicado)
          this.mensagemErro = err.error.erro + '  konflikters '; // Adicionei um emoji para teste
        } else if (err.status === 500 && err.error?.erro) {
            // Erro interno do servidor
            this.mensagemErro = err.error.erro + '  серверная ошибка ';
        }
         else {
          this.mensagemErro = 'Erro ao tentar cadastrar usuário. Tente novamente mais tarde. 😥';
        }
      }
    });
  }
  permitirSomenteLetras(event: KeyboardEvent) {
  const regex = /^[A-Za-zÀ-ÿ\s]$/;
  const inputChar = event.key;

  if (!regex.test(inputChar)) {
    event.preventDefault();
  }
}
}