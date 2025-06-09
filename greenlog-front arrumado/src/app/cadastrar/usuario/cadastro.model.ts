export interface Usuario {
  username: string;
  nome: string;
  cpf: string;
  idade: number | null;
  telefone: string;
  genero: string;
  senha: string;
}

export interface UsuarioResponse {
  id: number;
  username: string;
  nome: string;
  cpf: string;
}