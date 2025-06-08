import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';

import { SidebarComponent } from './padronizador/menu/sidebar/sidebar.component';
import { TopbarComponent } from './padronizador/menu/topbar/topbar.component';
import { CommonModule } from '@angular/common';


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