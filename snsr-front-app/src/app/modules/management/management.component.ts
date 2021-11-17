import {Component, Input, OnInit} from '@angular/core';
import {BaseStation} from '../../shared/models/base.station';
import {BaseStationService} from '../../shared/services/base.station.service';
import {SensorService} from '../../shared/services/sensor.service';
import {Sensor} from '../../shared/models/sensor';
import {ClrDatagridStateInterface} from '@clr/angular';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent implements OnInit {

  detail: BaseStation;
  stations: BaseStation[];
  editModalOpen: boolean;
  loading: boolean;

  constructor(private baseStationService: BaseStationService,
              private  sensorService: SensorService) {
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.loading = true;
    this.detail = null;
    this.editModalOpen = false;
    this.baseStationService.getAllBaseStations().subscribe((stations) => {
      this.stations = stations;
      this.loading = false;
    });
  }

  changeStationState(id: number) {
    this.baseStationService.changeStationState(id).subscribe(() => {
      const ind = this.stations.findIndex((value) => value.id === id);
      this.stations[ind].registered = !!this.stations[ind].registered;
    });
  }

  changeSensorState(stationId: number, sensorId: number) {
    this.sensorService.changeSensorState(sensorId).subscribe(() => {
      const stationInd = this.stations.findIndex(value => value.id === stationId);
      const sensorInd = this.stations[stationInd].sensors.findIndex((value) => value.id === sensorId);
      this.stations[stationInd].sensors[sensorInd].registered = !!this.stations[stationInd].sensors[sensorInd].registered;
    });
  }

  deleteStation(id: number) {
    this.baseStationService.deleteStation(id).subscribe(() => this.refresh());
  }

  deleteSensor(stationId: number, sensorId: number) {
    this.sensorService.deleteSensor(sensorId).subscribe(() => {
      const stationInd = this.stations.findIndex(value => value.id === stationId);
      const sensorInd = this.stations[stationInd].sensors.findIndex((value) => value.id === sensorId);
      this.stations[stationInd].sensors.splice(sensorInd, 1);
    });
  }
}
