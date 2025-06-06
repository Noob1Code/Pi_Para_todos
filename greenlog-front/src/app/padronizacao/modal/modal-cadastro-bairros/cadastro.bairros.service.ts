import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bairros } from './cadastro.bairros.model';

@Injectable({
  providedIn: 'root'
})
export class CadastroBairrosService {

  private apiUrl = 'http://localhost:8080/api/bairro';

  constructor(private http: HttpClient) {}

  getBairros(): Observable<Bairros[]> {
    return this.http.get<Bairros[]>(this.apiUrl);
  }

  adicionarBairros(bairros: Bairros): Observable<void> {
    return this.http.post<void>(this.apiUrl, bairros);
  }
  
  atualizarBairros(id: number, bairros: Bairros): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, bairros); // corrigido
  }
    
  excluirBairros(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`); // corrigido
  }  
}