import { Bairro } from "../bairro/bairro.model";

export interface PontoColeta {
  id?: number; 
  bairro: Bairro;
  nome: string;
  responsavel: string;
  telefoneResponsavel?: string; 
  emailResponsavel?: string; 
  endereco?: string;
  horarioFuncionamento?: string;
  tiposResiduosAceitos: string[];
}

export interface PontoColetaUPDATE{
  bairro: { id: number | undefined },
  nome: string;
  responsavel: string;
  telefoneResponsavel?: string; 
  emailResponsavel?: string; 
  endereco?: string;
  horarioFuncionamento?: string;
  tiposResiduosAceitos: string[];
}