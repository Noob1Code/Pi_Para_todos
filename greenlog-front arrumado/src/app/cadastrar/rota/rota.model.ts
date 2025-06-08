import { Caminhao } from "../caminhao/caminhao.modal";
import { PontoColeta } from "../ponto-coleta/ponto-coleta.model";

export interface Rota{
    id?: number;
    caminhao: Caminhao;
    origem: PontoColeta;
    destino: PontoColeta;
    tipoResiduo: string;
}