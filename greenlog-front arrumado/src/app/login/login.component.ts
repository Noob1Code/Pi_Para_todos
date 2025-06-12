// src/app/login/login.component.ts

import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
// O LoginService não é mais necessário aqui.
// import { LoginService } from './login.service';
import { Login } from './login.model';
import { AuthService } from '../authService'; // Apenas o AuthService é necessário

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // Mantém os campos do formulário
  usuario: string = '';
  senha: string = '';
  mensagemErro: string = '';
  
  // Remove a injeção do LoginService, deixando apenas AuthService e Router
  constructor(
    private router: Router,
    private authService: AuthService 
  ) {}

  onSubmit() {
    this.mensagemErro = '';

    if (this.usuario && this.senha) {
      
      // Chama o método de login centralizado do AuthService
      this.authService.login({ username: this.usuario, senha: this.senha }).subscribe({
        next: (response: Login) => {
          // A lógica de armazenar o utilizador e o token já está dentro do AuthService.
          // A única responsabilidade aqui é navegar para a próxima página.
          this.router.navigate(['/menu']); 
        },
        error: (err) => {
          // A lógica de tratamento de erro permanece a mesma.
          console.error('Erro no login:', err);
          if (err.status === 401) {
            this.mensagemErro = 'Utilizador ou senha inválidos.';
          } else if (err.error?.erro) {
            this.mensagemErro = err.error.erro;
          } else {
            this.mensagemErro = 'Erro ao tentar fazer login. Verifique a sua conexão ou tente novamente mais tarde.';
          }
        }
      });

    } else {
      this.mensagemErro = 'Por favor, preencha o utilizador e a senha.';
      console.error('Utilizador ou senha não preenchidos.');
    }
  }
}
