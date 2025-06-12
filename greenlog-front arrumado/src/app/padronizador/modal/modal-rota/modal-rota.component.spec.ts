import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalRotaComponent } from './modal-rota.component';

describe('ModalRotaComponent', () => {
  let component: ModalRotaComponent;
  let fixture: ComponentFixture<ModalRotaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalRotaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalRotaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
