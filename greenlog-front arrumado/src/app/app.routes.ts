import { Routes } from "@angular/router";
import { BairroComponent } from "./cadastrar/bairro/bairro.component";
import { CaminhaoComponent } from "./cadastrar/caminhao/caminhao.component";
import { RuaComponent } from "./cadastrar/rua/rua.component";
import { RotaComponent } from "./cadastrar/rota/rota.component";
import { PontoColetaComponent } from "./cadastrar/ponto-coleta/ponto-coleta.component";
import { CadastroUsuarioComponent } from "./cadastrar/usuario/cadastro.component";
import { LoginComponent } from "./login/login.component";
import { DashboardComponent } from "./menu/dashboard/dashboard.component";


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroUsuarioComponent },
  { path: 'menu', component: DashboardComponent },
  { path: 'ruas', component: RuaComponent },
  { path: 'rotas', component: RotaComponent },
  { path: 'pontos-coleta', component: PontoColetaComponent },
  { path: 'caminhoes', component: CaminhaoComponent },
  { path: 'bairros', component: BairroComponent }
];