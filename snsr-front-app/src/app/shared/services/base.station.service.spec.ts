import {TestBed} from '@angular/core/testing';

import {BaseStationService} from './base.station.service';

describe('Base.StationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BaseStationService = TestBed.get(BaseStationService);
    expect(service).toBeTruthy();
  });
});
