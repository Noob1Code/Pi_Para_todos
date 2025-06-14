import { Bairro } from "../bairro/bairro.model";
import { Caminhao } from "../caminhao/caminhao.modal";
import { Rua } from "../rua/rua.model";
import { PontoColeta } from '../ponto-coleta/ponto-coleta.model';

export interface Rota {
  id?: number;
  caminhao: Caminhao | null;
  destino: PontoColeta | null; 
  tipoResiduo: string;
  bairrosPercorridos: string[];
  arestasPercorridas?: Rua[]; 
  distanciaTotal: number;      
}

export interface CaminhoDTO {
  bairros: Bairro[];
  arestas: Rua[];
  distanciaTotal: number;
}

export interface RotaUPDATE {
  caminhaoId: { id: number | undefined };
  destinoId: { id: number | undefined };
  tipoResiduo: string;
}
