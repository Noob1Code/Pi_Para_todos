import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Caminhao } from './caminhao.modal';

@Injectable({
  providedIn: 'root'
})
export class CaminhaoService {
    
  private apiUrl = 'http://localhost:8080/api/caminhoes';

  constructor(private http: HttpClient) {}

  listar(): Observable<Caminhao[]> {
    return this.http.get<Caminhao[]>(this.apiUrl);
  }

  salvar(caminhao: Caminhao): Observable<Caminhao> {
    return this.http.post<Caminhao>(this.apiUrl, caminhao);
  }

  atualizar(id: number, caminhao: Caminhao): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, caminhao);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}