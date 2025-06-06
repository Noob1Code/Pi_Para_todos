import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bairro } from './bairro.model';

@Injectable({
  providedIn: 'root'
})
export class BairroService {
  private apiUrl = 'http://localhost:8080/api/bairros';

  constructor(private http: HttpClient) {}

  getBairros(): Observable<Bairro[]> {
    return this.http.get<Bairro[]>(this.apiUrl);
  }

  adicionarBairro(bairro: Bairro): Observable<Bairro> {
    return this.http.post<Bairro>(this.apiUrl, bairro);
  }

  atualizarBairro(id: number, bairro: Bairro): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, bairro);
  }

  excluirBairro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}