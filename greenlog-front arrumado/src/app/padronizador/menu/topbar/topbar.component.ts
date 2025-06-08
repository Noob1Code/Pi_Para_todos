import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css'] // ✅ Corrigido aqui (era styleUrl)
})
export class TopbarComponent {
  profileMenuOpen = false;

  constructor(
    private layoutService: LayoutService,
    private router: Router
  ) {}

  toggleSidebar(): void {
    this.layoutService.toggleSidebar();
  }

  toggleProfileMenu(): void {
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  logout(): void {
    // Aqui você pode adicionar lógica de autenticação futuramente
    alert('Usuário deslogado.');
    this.router.navigate(['/login']);
  }
}
