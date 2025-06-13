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
    const payload = this.padronizacao(itinerario);
    return this.http.post<ItinerarioUPDATE>(this.apiUrl, payload).pipe(
      catchError((error: HttpErrorResponse) => throwError(() => error))
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