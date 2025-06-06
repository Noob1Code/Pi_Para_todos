import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PontoColeta } from './ponto-coleta.model';

@Injectable({
  providedIn: 'root'
})
export class PontoColetaService {
  private apiUrl = 'http://localhost:8080/api/pontos';

  constructor(private http: HttpClient) {}

  getPontosColeta(): Observable<PontoColeta[]> {
    return this.http.get<PontoColeta[]>(this.apiUrl).pipe(catchError(this.handleError));
  }

  getPontoColeta(id: number): Observable<PontoColeta> {
    return this.http.get<PontoColeta>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  adicionarPontoColeta(pontoColeta: PontoColeta): Observable<PontoColeta> {
    return this.http.post<PontoColeta>(this.apiUrl, pontoColeta).pipe(catchError(this.handleError));
  }

  atualizarPontoColeta(id: number, pontoColeta: PontoColeta): Observable<PontoColeta> {
    return this.http.put<PontoColeta>(`${this.apiUrl}/${id}`, pontoColeta).pipe(catchError(this.handleError));
  }

  excluirPontoColeta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    // Lógica de tratamento de erro genérica para o frontend
    // Você pode customizar isso para mostrar mensagens mais amigáveis
    console.error('Ocorreu um erro na API:', error);
    let errorMessage = 'Erro desconhecido!';
    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else if (error.status) {
      // Erro do lado do servidor
      errorMessage = `Código do erro: ${error.status}\nMensagem: ${error.error?.erro || error.message}`;
      if (error.status === 400 && error.error) { // Erros de validação
        const validationErrors = error.error;
        let messages = [];
        for (const key in validationErrors) {
          if (validationErrors.hasOwnProperty(key)) {
            messages.push(validationErrors[key]);
          }
        }
        errorMessage = `Erros de validação: ${messages.join(', ')}`;
      }
    }
    return throwError(() => new Error(errorMessage));
  }
}

export type { PontoColeta };
