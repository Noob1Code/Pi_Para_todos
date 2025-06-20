import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BairroComponent } from './bairro.component';

describe('BairroComponent', () => {
  let component: BairroComponent;
  let fixture: ComponentFixture<BairroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BairroComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BairroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
