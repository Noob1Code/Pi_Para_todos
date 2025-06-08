import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalCaminhaoComponent } from './modal-caminhao.component';

describe('ModalCaminhaoComponent', () => {
  let component: ModalCaminhaoComponent;
  let fixture: ComponentFixture<ModalCaminhaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalCaminhaoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalCaminhaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
