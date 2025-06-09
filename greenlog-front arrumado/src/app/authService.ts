import { Injectable } from "@angular/core";
import { Login } from "./login/login.model";

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usuario: Login | null = null;

  setUsuario(usuario: Login) {
    this.usuario = usuario;
  }

  getUsuario(): Login | null {
    return this.usuario;
  }

  getNome(): string | null {
    return this.usuario?.nome ?? null;
  }
}
