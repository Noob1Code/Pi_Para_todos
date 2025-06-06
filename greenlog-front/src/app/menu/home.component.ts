import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TopbarComponent } from "../padronizacao/menu/topbar/topbar.component";
import { SidebarComponent } from "../padronizacao/menu/sidebar/sidebar.component";

@Component({
  selector: 'app-home',
  imports: [CommonModule, TopbarComponent, SidebarComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
}
