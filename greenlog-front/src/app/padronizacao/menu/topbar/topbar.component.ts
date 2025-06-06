import { Component } from '@angular/core';
import { LayoutService } from '../layout.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-topbar',
  imports: [],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.css'
})
export class TopbarComponent {

  constructor(private layoutService: LayoutService, private router: Router) {}

  toggleSidebar() {
    this.layoutService.toggleSidebar();
  }

  profileMenuOpen = false;

  toggleProfileMenu() {
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  logout(): void {
    alert('Usuário deslogado.');
    this.router.navigate(['/login']);
  }

}
