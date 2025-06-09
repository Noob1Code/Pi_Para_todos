import { Bairro } from "../bairro/bairro.model";
import { Caminhao } from "../caminhao/caminhao.modal";

export interface Rota {
  id?: number;
  caminhao: Caminhao|null;
  destino: Bairro|null;
  tipoResiduo: string;
  bairrosPercorridos: string[];  // Nomes dos bairros ordenados
  distanciaTotal: number;        // Distância em km
}