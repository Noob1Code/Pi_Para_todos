import { Interface } from "readline";
import { Bairro } from "../bairro/bairro.model";

export interface Rua {
  id?: number;
  nome: string;
  origem: Bairro;
  destino: Bairro;
  distancia: number|null;
}

export interface RuaUPDATE{
  nome: String
  origem: { id: number | undefined };
  destino: { id: number | undefined };
  distancia: Number
}