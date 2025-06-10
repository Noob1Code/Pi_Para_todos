import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CaminhoDTO, Rota } from "./rota.model";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class RotaService {
    
  private readonly apiUrl = 'http://localhost:8080/api/rotas';
  private readonly caminhoUrl = 'http://localhost:8080/api/caminhos';

  constructor(private http: HttpClient) {}

  listar(): Observable<Rota[]> {
    return this.http.get<Rota[]>(this.apiUrl);
  }

  salvar(rota: Rota): Observable<Rota> {
    return this.http.post<Rota>(this.apiUrl, rota);
  }

  atualizar(id: number, rota: Rota): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, rota);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  calcularRota(destinoId: number): Observable<CaminhoDTO> {
    return this.http.get<CaminhoDTO>(`${this.caminhoUrl}/menor-caminho`, {
      params: { destinoId }
    });
  }
}