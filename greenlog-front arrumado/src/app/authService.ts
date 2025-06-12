import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Login } from './login/login.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usuarioLogadoSubject = new BehaviorSubject<Login | null>(null);
  public usuarioLogado$ = this.usuarioLogadoSubject.asObservable();

  private apiUrl = 'http://localhost:8080/api/usuarios'; 

  constructor(private http: HttpClient, private router: Router) {
    this.carregarUsuarioDoLocalStorage();
  }

  login(loginData: { username: string; senha: string }): Observable<Login> {
    const { username, senha } = loginData;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Basic ' + btoa(`${username}:${senha}`),
    });

    return this.http
      .post<Login>(`${this.apiUrl}/login`, { username, senha }, { headers })
      .pipe(
        tap((usuario: Login) => {
          if (typeof window !== 'undefined' && window.localStorage) {
            localStorage.setItem('usuarioLogado', JSON.stringify(usuario));
            localStorage.setItem('authHeader', headers.get('Authorization')!);
          }
          this.usuarioLogadoSubject.next(usuario);
        })
      );
  }

  logout() {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.removeItem('usuarioLogado');
      localStorage.removeItem('authHeader');
    }
    this.usuarioLogadoSubject.next(null);
    this.router.navigate(['/login']);
  }

  getUsuarioLogado(): Login | null {
    return this.usuarioLogadoSubject.value;
  }
  
  getAuthHeader(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('authHeader');
    }
    return null;
  }

  private carregarUsuarioDoLocalStorage() {
    if (typeof window !== 'undefined' && window.localStorage) {
      const usuarioJson = localStorage.getItem('usuarioLogado');
      if (usuarioJson) {
        this.usuarioLogadoSubject.next(JSON.parse(usuarioJson));
      }
    }
  }
}