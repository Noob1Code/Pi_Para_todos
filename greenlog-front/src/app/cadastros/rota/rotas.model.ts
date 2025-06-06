import { Caminhao } from "../caminhao/caminhao.service";
import { PontoColeta } from "../pontos-coleta/ponto-coleta.model";

export class Rota {
  id?: number;
  caminhao!: Caminhao; // <- objeto inteiro
  data!: string;
  origem!: PontoColeta;
  destino!: PontoColeta;

  constructor(caminhao: Caminhao, data: string, origem: PontoColeta, destino: PontoColeta) {
    this.caminhao = caminhao;
    this.data = data;
    this.origem = origem;
    this.destino = destino;
  }
}
