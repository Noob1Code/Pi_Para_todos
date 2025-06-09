export interface Caminhao {
  id?: number;
  placa: string;
  motorista: string;
  capacidade: number |null;
  residuos: string[];
}