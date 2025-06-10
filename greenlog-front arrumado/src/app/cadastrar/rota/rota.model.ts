import { Bairro } from "../bairro/bairro.model";
import { Caminhao } from "../caminhao/caminhao.modal";

export interface Rota {
  id?: number;
  caminhao: Caminhao|null;
  destino: Bairro|null;
  tipoResiduo: string;
  bairrosPercorridos: string[];
  arestasPercorridas?: Aresta[]; 
  distanciaTotal: number;      
}

export interface CaminhoDTO {
  bairros: Bairro[];
  arestas: Aresta[];
  distanciaTotal: number;
}

export interface Aresta {
  rua: string;
  origemId: number;
  destinoId: number;
  quilometros: number;
}