import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LayoutService } from '../layout.service';
import { AuthService } from '../../../authService';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css']
})
export class TopbarComponent implements OnInit{
  profileMenuOpen = false;
  nome: string = '';

  constructor(
    private layoutService: LayoutService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (typeof window !== 'undefined' && window.localStorage) {
      const usuarioStr = localStorage.getItem('usuarioLogado');
      if (usuarioStr) {
        const usuario = JSON.parse(usuarioStr);
        this.nome = usuario.nome;
      }
    }
  } 
  toggleSidebar(): void {
    this.layoutService.toggleSidebar();
  }

  toggleProfileMenu(): void {
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  logout() {
  localStorage.removeItem('usuarioLogado');
  this.router.navigate(['/login']);
  }

  configuracaoUser(){
    this.router.navigate(['/cadastro']);
  }
}
