import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeForm } from './me-form';

describe('MeForm', () => {
  let component: MeForm;
  let fixture: ComponentFixture<MeForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MeForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
