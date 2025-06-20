import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { CaminhoDTO, Rota, RotaUPDATE } from "./rota.model";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class RotaService {
    
  private readonly apiUrl = 'http://localhost:8080/api/rotas';
  private readonly caminhoUrl = 'http://localhost:8080/api/rotas/calcular';

  constructor(private http: HttpClient) {}

  listar(): Observable<Rota[]> {
    return this.http.get<Rota[]>(this.apiUrl);
  }

  salvar(rota: Rota): Observable<RotaUPDATE> {
    return this.http.post<RotaUPDATE>(this.apiUrl, this.padronizacao(rota));
  }

  atualizar(id: number, rota: Rota): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, rota);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  calcularRota(destinoBairroId: number): Observable<CaminhoDTO> {
    return this.http.get<CaminhoDTO>(`${this.caminhoUrl}`, {
      params: { destinoId: destinoBairroId }
    });
  }

  private padronizacao(rota: Rota): RotaUPDATE {
    if (!rota.caminhao || !rota.destino) {
      throw new Error("Caminhão e Destino são obrigatórios para salvar a rota.");
    }
    return {
      caminhaoId: { id: rota.caminhao.id },
      destinoId: { id: rota.destino.bairro.id }, 
      tipoResiduo: rota.tipoResiduo
    };
  }
}
