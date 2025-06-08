import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPontoColetaComponent } from './modal-ponto-coleta.component';

describe('ModalPontoColetaComponent', () => {
  let component: ModalPontoColetaComponent;
  let fixture: ComponentFixture<ModalPontoColetaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalPontoColetaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalPontoColetaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
