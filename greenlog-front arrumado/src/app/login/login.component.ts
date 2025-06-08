import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'; // 1. Importar HttpClient e HttpErrorResponse

// Opcional: Definir uma interface para a resposta do login, baseada no UsuarioResponseDTO
interface UsuarioResponse {
  id: number;
  username: string;
  nome: string;
  cpf: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink
    // HttpClientModule não é mais necessário aqui se provideHttpClient() estiver em app.config.ts
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // Os nomes 'usuario' e 'senha' no template serão mapeados para 'username' e 'senha' no DTO do backend.
  usuario: string = ''; // Este campo corresponde a 'username' no LoginRequestDTO
  senha: string = '';   // Este campo corresponde a 'senha' no LoginRequestDTO
  mensagemErro: string = '';

  // 5. URL do endpoint de login do backend
  // Ajuste a porta se o seu backend Spring Boot rodar em uma diferente (padrão é 8080)
  private apiUrl = 'http://localhost:8080/api/usuarios/login';

  constructor(
    private router: Router,
    private http: HttpClient // 2. Injetar HttpClient
  ) { }

  onSubmit() {
    this.mensagemErro = ''; // Limpa mensagens de erro anteriores

    if (this.usuario && this.senha) {
      // O backend espera um objeto com 'username' e 'senha'
      const loginData = {
        username: this.usuario,
        senha: this.senha
      };

      // 3. Chamar o endpoint de login
      this.http.post<UsuarioResponse>(this.apiUrl, loginData).subscribe({
        next: (response) => {
          console.log('Login bem-sucedido:', response);

          // Opcional: Armazenar dados do usuário (ex: no localStorage)
          // localStorage.setItem('usuarioLogado', JSON.stringify(response));
          // localStorage.setItem('token', response.token); // Se você implementar JWT no futuro

          // Navegar para a tela de menu
          this.router.navigate(['/menu']);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Erro no login:', err);
          if (err.status === 401) {
            // O backend retorna um corpo como { "erro": "Usuário ou senha inválidos." }
            this.mensagemErro = err.error?.erro || 'Usuário ou senha inválidos.';
          } else if (err.error && err.error.erro) {
             // Para outras mensagens de erro vindas do backend que seguem o padrão {"erro": "..."}
            this.mensagemErro = err.error.erro;
          }
          else {
            this.mensagemErro = 'Erro ao tentar fazer login. Verifique sua conexão ou tente novamente mais tarde.';
          }
        }
      });
    } else {
      this.mensagemErro = 'Por favor, preencha o usuário e a senha.';
      console.error('Usuário ou senha não preenchidos.');
    }
  }
}