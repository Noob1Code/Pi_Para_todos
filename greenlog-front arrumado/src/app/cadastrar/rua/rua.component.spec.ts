import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuaComponent } from './rua.component';

describe('RuaComponent', () => {
  let component: RuaComponent;
  let fixture: ComponentFixture<RuaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RuaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RuaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
