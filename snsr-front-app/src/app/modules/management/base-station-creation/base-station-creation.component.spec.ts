import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseStationCreationComponent } from './base-station-creation.component';

describe('BaseStationCreationComponent', () => {
  let component: BaseStationCreationComponent;
  let fixture: ComponentFixture<BaseStationCreationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BaseStationCreationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseStationCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
