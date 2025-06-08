import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PontoColetaComponent } from './ponto-coleta.component';

describe('PontoColetaComponent', () => {
  let component: PontoColetaComponent;
  let fixture: ComponentFixture<PontoColetaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PontoColetaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PontoColetaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
