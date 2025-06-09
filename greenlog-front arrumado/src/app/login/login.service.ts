import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Login } from './login.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiUrl = 'http://localhost:8080/api/usuarios/login';

  constructor(private http: HttpClient) {}

  login(username: string, senha: string): Observable<Login> {
    const loginData = { username, senha };
    return this.http.post<Login>(this.apiUrl, loginData).pipe(
      catchError((error: HttpErrorResponse) => throwError(() => error))
    );
  }

  
}