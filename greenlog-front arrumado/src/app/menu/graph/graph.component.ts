import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import cytoscape from 'cytoscape';
import { BairroService } from '../../cadastrar/bairro/bairro.service';
import { RuaService } from '../../cadastrar/rua/rua.service';
import { Bairro } from '../../cadastrar/bairro/bairro.model';
import { Rua } from '../../cadastrar/rua/rua.model';
import coseBilkent from 'cytoscape-cose-bilkent';

// ✅ Registra o layout FORA da classe
cytoscape.use(coseBilkent);

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.css']
})
export class GraphComponent implements AfterViewInit {
  @ViewChild('cy') cyRef!: ElementRef;

  constructor(
    private bairroService: BairroService,
    private ruaService: RuaService
  ) {}

  ngAfterViewInit(): void {
    this.carregarGrafo();
  }

  carregarGrafo(): void {
    this.bairroService.listar().subscribe(bairros => {
      this.ruaService.listar().subscribe(ruas => {
        const elementos: any[] = [];

        bairros.forEach((bairro: Bairro) => {
          elementos.push({
            data: { id: bairro.nome, label: bairro.nome }
          });
        });

        ruas.forEach((rua: Rua) => {
          elementos.push({
            data: {
              source: rua.origem.nome,
              target: rua.destino.nome,
              label: `${rua.nome} - ${rua.distancia}km`
            }
          });
        });

        // ✅ Layout usando cose-bilkent corretamente
        const cy = cytoscape({
  container: this.cyRef.nativeElement,
  elements: elementos,
  style: [
  {
    selector: 'node',
    style: {
      'background-color': '#1abc9c',
      'label': 'data(label)',
      'color': '#ecf0f1',
      'text-valign': 'center',
      'text-halign': 'center',
      'width': 60,
      'height': 60,
      'font-size': '13px',
      'text-wrap': 'wrap',
      'text-max-width': '80',
      'transition-property': 'background-color, width, height',
      'transition-duration': 0.2,
      'border-color': '#ecf0f1',
      'border-width': 2
    }
  },
  {
    selector: 'node:hover',
    style: {
      'background-color': '#16a085',
      'width': 75,
      'height': 75,
      'font-size': '14px'
    }
  },
  {
    selector: 'edge',
    style: {
      'width': 2,
      'line-color': '#ecf0f1',
      'target-arrow-color': '#ecf0f1',
      'target-arrow-shape': 'triangle',
      'source-arrow-color': '#ecf0f1',
      'source-arrow-shape': 'triangle',
      'curve-style': 'bezier',
      'label': 'data(label)',
      'font-size': '10px',
      'text-rotation': 'autorotate',
      'text-margin-y': -10,
      'color': '#bdc3c7'
    }
  },
  {
    selector: 'edge:hover',
    style: {
      'line-color': '#3498db',
      'target-arrow-color': '#3498db',
      'source-arrow-color': '#3498db',
      'font-weight': 'bold',
      'font-size': '12px'
    }
  }
],
  layout: {
    name: 'cose-bilkent',
    idealEdgeLength: 150,
    nodeRepulsion: 4500,
    edgeElasticity: 0.45,
    nestingFactor: 0.1,
    gravity: 0.25,
    numIter: 2500,
    animate: true
  } as any,
  userZoomingEnabled: true,
  userPanningEnabled: true,
  wheelSensitivity: 3
});

// Interações extras
cy.on('tap', 'node', (event) => {
  const node = event.target;
  alert(`Bairro: ${node.data('label')}`);
});
      });
    });
  }
}
