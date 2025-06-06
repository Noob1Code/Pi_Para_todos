export class Conexao {
  constructor(
    public rua: string,
    public origem: string,
    public destino: string,
    public quilometros: number,
    public id?: number
  ) {}
}