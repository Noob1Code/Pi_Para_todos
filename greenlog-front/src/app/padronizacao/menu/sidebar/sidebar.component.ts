import { Component } from '@angular/core';
import { LayoutService } from '../layout.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  sidebarOpen = false;

  constructor(private layoutService: LayoutService,private router: Router) {}

  ngOnInit() {
    this.layoutService.sidebarOpen$.subscribe(open => {
      this.sidebarOpen = open;
    });
  }

  cadastroAberto = false;

  toggleCadastro() {
    this.cadastroAberto = !this.cadastroAberto;
  }

  navegarPara(destino: string) {
    if(destino == "Menu") this.router.navigate(['/menu']);
    if(destino == "Ruas") this.router.navigate(['/ruas']);
    if(destino == "Rotas") this.router.navigate(['/rotas']);
    if(destino == "Caminhao") this.router.navigate(['/caminhao']);
    if (destino === "PontosColeta") this.router.navigate(['/pontos-coleta']);
    if (destino === "Bairros") this.router.navigate(['/bairros']);
    
    console.log('Ir para:', destino);
  }
}
