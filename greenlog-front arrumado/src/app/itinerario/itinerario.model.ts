import { Rota } from "../cadastrar/rota/rota.model";

export interface Itinerario {
  id?: number;
  data: Date;
  rota: Rota;
}

export interface ItinerarioUPDATE{
  data: Date;
  rotaId: { id: number | undefined };
}