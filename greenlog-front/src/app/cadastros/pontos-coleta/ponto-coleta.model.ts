import { Bairro } from '../bairros/bairro.model';

export interface PontoColeta {
  id?: number; 
  bairro: Bairro | null;
  nome: string;
  responsavel: string;
  telefoneResponsavel?: string; 
  emailResponsavel?: string; 
  endereco?: string;
  horarioFuncionamento?: string;
  tiposResiduosAceitos: string[];
}