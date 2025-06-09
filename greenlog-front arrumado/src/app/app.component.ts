import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import localePt from '@angular/common/locales/pt';
import { SidebarComponent } from './padronizador/menu/sidebar/sidebar.component';
import { TopbarComponent } from './padronizador/menu/topbar/topbar.component';
import { CommonModule, registerLocaleData } from '@angular/common';

registerLocaleData(localePt);

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, SidebarComponent, TopbarComponent,CommonModule
],
  templateUrl: './app.component.html', 
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'minha-app-angular';

  constructor(public router: Router) {}

  isLayoutVisible(): boolean {
    const hiddenRoutes = ['/login', '/cadastro']; // adicione mais se quiser
    return !hiddenRoutes.includes(this.router.url);
  }

}