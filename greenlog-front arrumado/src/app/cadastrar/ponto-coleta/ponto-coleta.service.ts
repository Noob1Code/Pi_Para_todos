import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { PontoColeta, PontoColetaUPDATE } from "./ponto-coleta.model";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class PontoColetaService {
    
  private apiUrl = 'http://localhost:8080/api/pontos';

  constructor(private http: HttpClient) {}

  listar(): Observable<PontoColeta[]> {
    return this.http.get<PontoColeta[]>(this.apiUrl);
  }

  salvar(pontoColeta: PontoColeta): Observable<PontoColetaUPDATE> { // Retorna o tipo PontoColeta, que é similar ao ResponseDTO
    const dadosParaSalvar = this.padronizacao(pontoColeta);
    return this.http.post<PontoColetaUPDATE>(this.apiUrl, dadosParaSalvar);
  }

  atualizar(id: number, pontoColeta: PontoColeta): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, this.padronizacao(pontoColeta));
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  padronizacao(pontoColeta: PontoColeta): PontoColetaUPDATE {
  return {
    bairro: { id: pontoColeta.bairro.id },
    nome: pontoColeta.nome,
    responsavel: pontoColeta.responsavel,
    telefoneResponsavel: pontoColeta.telefoneResponsavel,
    emailResponsavel: pontoColeta.emailResponsavel,
    endereco: pontoColeta.endereco,
    horarioFuncionamento: pontoColeta.horarioFuncionamento,
    tiposResiduosAceitos: pontoColeta.tiposResiduosAceitos,
  };
}
}