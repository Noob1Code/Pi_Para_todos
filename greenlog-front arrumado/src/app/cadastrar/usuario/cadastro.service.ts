import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Usuario,UsuarioResponse } from './cadastro.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CadastroUsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios/cadastrar';

  constructor(private http: HttpClient) {}

  cadastrar(usuario: Usuario): Observable<UsuarioResponse> {
    const dadosParaBackend = {
      ...usuario,
      cpf: usuario.cpf.replace(/\D/g, ''),
      telefone: usuario.telefone.replace(/\D/g, '')
    };
    return this.http.post<UsuarioResponse>(this.apiUrl, dadosParaBackend);
  }

  buscarPorId(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }
}
