import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LayoutService } from '../layout.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  sidebarOpen = false;
  cadastroAberto = false;

  constructor(
    private layoutService: LayoutService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.layoutService.sidebarOpen$.subscribe((open) => {
      this.sidebarOpen = open;
    });
  }

  toggleCadastro(): void {
    this.cadastroAberto = !this.cadastroAberto;
  }

  fecharSidebar(): void {
    this.layoutService.toggleSidebar();
  }

  navegarPara(destino: string): void {
    switch (destino) {
      case 'Menu':
        this.router.navigate(['/menu']);
        break;
      case 'Ruas':
        this.router.navigate(['/ruas']);
        break;
      case 'Rotas':
        this.router.navigate(['/rotas']);
        break;
      case 'Caminhao':
        this.router.navigate(['/caminhoes']);
        break;
      case 'PontosColeta':
        this.router.navigate(['/pontos-coleta']);
        break;
      case 'Bairros':
        this.router.navigate(['/bairros']);
        break;
      case 'Intinerario':
        this.router.navigate(['/intinerario']);
        break;
    }

    console.log('Ir para:', destino);
  }
}
