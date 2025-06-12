import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Itinerario,ItinerarioUPDATE } from './itinerario.model';

@Injectable({
  providedIn: 'root'
})
export class ItinerarioService {
  private apiUrl = 'http://localhost:8080/api/itinerarios';

  constructor(private http: HttpClient) {}

  listar(): Observable<Itinerario[]> {
    return this.http.get<Itinerario[]>(this.apiUrl);
  }

  salvar(itinerario: Itinerario): Observable<ItinerarioUPDATE> {
    console.log(this.padronizacao(itinerario))
    return this.http.post<ItinerarioUPDATE>(this.apiUrl, this.padronizacao(itinerario));
  }

  atualizar(id: string, itinerario: Itinerario): Observable<Itinerario> {
    return this.http.put<Itinerario>(`${this.apiUrl}/${id}`, itinerario);
  }

  excluir(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  validarItinerario(data: Date, rotaNome: string): Observable<{ valido: boolean }> {
    return this.http.post<{ valido: boolean }>(`${this.apiUrl}/validar`, {
      data,
      rota: rotaNome
    });
  }

 padronizacao(itinerario: Itinerario): ItinerarioUPDATE {
      return {
        data: itinerario.data,
        rotaId: { id: itinerario.rota.id }
      };
    }
}
