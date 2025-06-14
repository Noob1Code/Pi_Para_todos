import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { Login } from './login.model';
import { AuthService } from '../authService'; 

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
  usuario: string = '';
  senha: string = '';
  mensagemErro: string = '';
  
  constructor(
    private router: Router,
    private authService: AuthService 
  ) {}

  onSubmit() {
    this.mensagemErro = '';

    if (this.usuario && this.senha) {
      this.authService.login({ username: this.usuario, senha: this.senha }).subscribe({
        next: (response: Login) => {
          this.router.navigate(['/menu']); 
        },
        error: (err) => {
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
