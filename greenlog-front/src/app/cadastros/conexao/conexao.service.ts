import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conexao } from './conexao.modulo';

@Injectable({
  providedIn: 'root'
})

export class ConexaoService {

    private apiUrl = "http://localhost:8080/api/conexaos";

    constructor(private http: HttpClient) {}

    getConexoes(): Observable<Conexao[]> {
        return this.http.get<Conexao[]>(this.apiUrl);
    }
    
    adicionarConexoes(conexao: Conexao): Observable<void> {
        return this.http.post<void>(this.apiUrl, conexao);
    }
    
    atualizarConexoes(id: number, conexao: Conexao): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/${id}`, conexao); // corrigido
    }
      
    excluirConexoes(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`); // corrigido
    }  
}