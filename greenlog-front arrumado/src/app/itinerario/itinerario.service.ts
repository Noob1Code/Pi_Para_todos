import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Itinerario, ItinerarioUPDATE } from './itinerario.model';

@Injectable({
  providedIn: 'root'
})
export class ItinerarioService {
  private apiUrl = 'http://localhost:8080/api/itinerarios';

  constructor(private http: HttpClient) {}

  listar(): Observable<Itinerario[]> {
    return this.http.get<Itinerario[]>(this.apiUrl).pipe(
      catchError((error: HttpErrorResponse) => throwError(() => error))
    );
  }

  salvar(itinerario: Itinerario): Observable<ItinerarioUPDATE> {
    console.log(this.padronizacao(itinerario))
    return this.http.post<ItinerarioUPDATE>(this.apiUrl, this.padronizacao(itinerario)).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Erro completo recebido do backend:', error); // <-- Adicione esta linha
        if (error.status === 409) {
          console.error('Erro 409: Conflito detectado', error);
          const mensagemErro = error.error?.erro || 'Conflito: recurso já existe ou conflito detectado.';
          return throwError(() => new Error(mensagemErro));
        }
      return throwError(() => error);
    })
  );
}

  atualizar(id: number, itinerario: Itinerario): Observable<Itinerario> {
    const payload = this.padronizacao(itinerario);
    return this.http.put<Itinerario>(`${this.apiUrl}/${id}`, payload).pipe(
      catchError((error: HttpErrorResponse) => throwError(() => error))
    );
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError((error: HttpErrorResponse) => throwError(() => error))
    );
  }

  private padronizacao(itinerario: Itinerario): ItinerarioUPDATE {
    return {
      data: itinerario.data,
      rotaId: { id: itinerario.rota.id }
    };
  }
}