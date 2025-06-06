import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalBairrosComponent } from './modal-bairros.component';

describe('ModalBairrosComponent', () => {
  let component: ModalBairrosComponent;
  let fixture: ComponentFixture<ModalBairrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalBairrosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalBairrosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
