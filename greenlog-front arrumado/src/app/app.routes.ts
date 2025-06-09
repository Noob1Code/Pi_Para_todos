import { Routes } from "@angular/router";
import { BairroComponent } from "./cadastrar/bairro/bairro.component";
import { CaminhaoComponent } from "./cadastrar/caminhao/caminhao.component";
import { RuaComponent } from "./cadastrar/rua/rua.component";
import { RotaComponent } from "./cadastrar/rota/rota.component";
import { PontoColetaComponent } from "./cadastrar/ponto-coleta/ponto-coleta.component";
import { CadastroUsuarioComponent } from "./cadastrar/usuario/cadastro.component";
import { LoginComponent } from "./login/login.component";
import { DashboardComponent } from "./menu/dashboard/dashboard.component";
import { AuthGuard } from "./auth.guard";
import { IntinerarioComponent } from "./intinerario/intinerario.component";


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroUsuarioComponent },
  { path: 'menu', component: DashboardComponent, canActivate: [AuthGuard]},
  { path: 'ruas', component: RuaComponent, canActivate: [AuthGuard] },
  { path: 'rotas', component: RotaComponent, canActivate: [AuthGuard] },
  { path: 'pontos-coleta', component: PontoColetaComponent, canActivate: [AuthGuard] },
  { path: 'caminhoes', component: CaminhaoComponent, canActivate: [AuthGuard] },
  { path: 'bairros', component: BairroComponent, canActivate: [AuthGuard] },
  { path: 'intinerario', component: IntinerarioComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];