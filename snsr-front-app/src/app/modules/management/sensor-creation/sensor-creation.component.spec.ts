import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SensorCreationComponent } from './sensor-creation.component';

describe('SensorCreationComponent', () => {
  let component: SensorCreationComponent;
  let fixture: ComponentFixture<SensorCreationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SensorCreationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
