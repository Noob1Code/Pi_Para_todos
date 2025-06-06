import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Rota } from './rotas.model';

@Injectable({
  providedIn: 'root'
})
export class RotasService {

  private apiUrl = 'http://localhost:8080/api/rotas';

  constructor(private http: HttpClient) {}

  getRotas(): Observable<Rota[]> {
  return this.http.get<Rota[]>(this.apiUrl);
}

  adicionarRotas(rota: Rota): Observable<void> {
    console.log(rota);
    return this.http.post<void>(this.apiUrl, rota);
  }

  atualizarRotas(id: number, rota: Rota): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, rota); // corrigido
  }
  
  excluirRotas(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`); // corrigido
  }  
  
}