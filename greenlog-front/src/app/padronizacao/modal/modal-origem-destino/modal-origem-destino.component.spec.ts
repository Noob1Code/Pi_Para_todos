import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalOrigemDestinoComponent } from './modal-origem-destino.component';

describe('ModalOrigemDestinoComponent', () => {
  let component: ModalOrigemDestinoComponent;
  let fixture: ComponentFixture<ModalOrigemDestinoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalOrigemDestinoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalOrigemDestinoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
