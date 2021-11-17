import {Component, OnInit} from '@angular/core';
import {BaseStationService} from '../../shared/services/base.station.service';
import {BaseStation} from '../../shared/models/base.station';
import {SensorService} from '../../shared/services/sensor.service';
import {Sensor} from '../../shared/models/sensor';
import {SensorValueType} from '../../shared/models/sensor.value.type';
import {SensorValue} from '../../shared/models/sensor.value';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import * as moment from 'moment';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  googleMarkers = [];
  isMarkersLoaded = false;
  dataForGraph: any[] = [];
  labels = [];
  stations: BaseStation[];
  sensors: Sensor[];
  selectedStations: BaseStation[] = [];
  selectedSensors: Sensor[] = [];
  types: SensorValueType[] = [];
  selectedType: SensorValueType[] = [];
  showPlot = false;
  type: SensorValueType;
  dateForm: FormGroup;
  minDate: string;
  maxDate: string;
  stationLoading: boolean;
  sensorLoading: boolean;
  typeLoading: boolean;

  constructor(private baseStationService: BaseStationService,
              private sensorService: SensorService,
              private formBuilder: FormBuilder) {
    this.dateForm = formBuilder.group({
      firstDate: new FormControl(),
      secondDate: new FormControl()
    });
  }

  ngOnInit(): void {
    this.stationLoading = true;
    this.baseStationService.getAllActiveBaseStations().subscribe((stations) => {
      this.stations = stations;
      this.stationLoading = false;
    });
    this.dateForm.valueChanges.subscribe((value) => {
      const t1 = this.dateForm.get('firstDate').value;
      const t2 = this.dateForm.get('secondDate').value;
      if (t1 !== null && t2 !== null && moment(t1).valueOf() <= moment(t2).valueOf()) {
        this.render();
      }
    });
    this.getMarkersInfo();
  }

  // метод, который вызывается при изменении выбора станций
  stationSelectionChanged() {
    this.sensorLoading = true;
    this.sensors = [].concat(...this.selectedStations.map((station) => station.sensors)).filter(sensor => sensor.registered);
    this.sensorLoading = false;
  }

  sensorSelectionChanged() {
    this.selectedType = [];
    if (this.selectedSensors.length > 0) {
      this.typeLoading = true;
      this.sensorService.getCrossTypesBySensorIds(this.selectedSensors.map(sensor => sensor.id)).subscribe((types) => {
        if (this.types.length > 0) {
          this.types = [];
        }
        this.types.push(...types);
        this.typeSelectionChanged();
        this.typeLoading = false;
      });
    } else {
      this.showPlot = false;
      this.types = [];
    }
  }

  typeSelectionChanged() {
    if (this.selectedType.length > 2) {
      this.selectedType.splice(1);
    }
    this.showPlot = false;
    const dates = [] as number[];
    const sensorIds = this.selectedSensors.map((sensor) => sensor.id);
    if (this.selectedSensors.length > 0) {
      this.selectedType.forEach(el => {
        if (el) {
          this.sensorService.getValuesBySensorIdsAndTypeCode(sensorIds, el.code).subscribe((map) => {
              for (const [key, values] of Object.entries(map)) {
                dates.push(...values.map(value => value.receivedTime));
              }
            }
            , () => {
            }, () => {
              this.minDate = moment(Math.min(...dates)).format('YYYY-MM-DD');
              this.maxDate = moment(Math.max(...dates)).format('YYYY-MM-DD');
              if (this.dateForm.get('firstDate').value != null && this.dateForm.get('secondDate').value != null) {
                if (this.dateForm.get('firstDate').value <= this.dateForm.get('secondDate').value) {
                  this.render();
                }
              }
            });
        }
      });
    } else {
      this.selectedType = [];
    }
  }

  render() {
    const dataSets = [];
    let count = this.selectedType.length;
    this.dataForGraph = [];
    this.labels = [];
    this.showPlot = false;
    this.selectedType.forEach(selType => {
      const sensorIds = this.selectedSensors.map((sensor) => sensor.id);
      if (selType.code) {
        this.sensorService.getValuesBySensorIdsAndTypeCode(sensorIds, selType.code).subscribe((map) => {
          for (const [key, values] of Object.entries(map)) {
            values.sort((a, b) => moment(a.receivedTime).valueOf() > moment(b.receivedTime).valueOf() ? 1 : -1);
            const filteredValues = values.filter(value => {
              return value.receivedTime >= moment(this.dateForm.get('firstDate').value).valueOf() &&
                value.receivedTime <= moment(this.dateForm.get('secondDate').value).add(1, 'days').valueOf() - 1;
            }) as SensorValue[];
            if (filteredValues.length !== 0) {
              dataSets.push([selType, key, filteredValues.map(el => el.value)]);
              filteredValues.forEach(el => {
                const mom = moment(el.receivedTime).valueOf();
                if (!this.labels.includes(mom)) {
                  this.labels.push(mom);
                }
              });
            }
          }
        }, () => {
        }, () => {
          if (dataSets.length === 0) {
            this.dateForm.reset();
          } else {
            this.dataForGraph.push(dataSets);
            count--;
            if (count === 0) {
              this.showPlot = true;
            }
          }
        });
      }
    });
  }

  getMarkersInfo() {
    for (let i = 0; i < 2; i++) {
      this.googleMarkers.push({lat: 55.9829727, lng: 37.2093055 + i});
    }
    this.isMarkersLoaded = true;
  }
}
