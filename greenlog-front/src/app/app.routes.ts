import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component'; 
import { CadastroUsuarioComponent } from './cadastros/usuario/cadastro.component';
import { CaminhaoComponent } from './cadastros/caminhao/caminhao.component';
import { HomeComponent } from './menu/home.component';
import { RotasComponent } from './cadastros/rota/rotas.component';
import { PontosColetaComponent } from './cadastros/pontos-coleta/pontos-coleta.component';
import { ConexaoComponent } from './cadastros/conexao/conexao.component';
import { BairrosComponent } from './cadastros/bairros/bairros.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroUsuarioComponent },
  { path: 'menu', component: HomeComponent },
  { path: 'ruas', component: ConexaoComponent },
  { path: 'rotas', component: RotasComponent },
  { path: 'caminhao', component: CaminhaoComponent },
  { path: 'pontos-coleta', component: PontosColetaComponent},
  { path: 'bairros', component: BairrosComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];