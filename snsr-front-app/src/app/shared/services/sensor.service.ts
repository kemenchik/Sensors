import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {SensorValue} from '../models/sensor.value';
import {Sensor} from '../models/sensor';
import {SensorValueType} from '../models/sensor.value.type';
import {BaseStation} from '../models/base.station';

@Injectable({
  providedIn: 'root'
})
export class SensorService {
  private sensorUrl = `${environment.apiUrl}/sensor`;

  constructor(private http: HttpClient) {
  }

  getValuesBySensorIdsAndTypeCode(sensorIds: number[], code: string) {
    const url = `${this.sensorUrl}/type/${code}`;
    return this.http.post<Map<string, SensorValue[]>>(url, sensorIds);
  }

  getCrossTypesBySensorSerial(name: string) {
    const url = `${this.sensorUrl}/name/${name}`;
    return this.http.get<SensorValue[]>(url);
  }

  getSensorsByBaseStationId(baseStationId: number) {
     const url = `${this.sensorUrl}/station/${baseStationId}`;
     return this.http.get<Sensor[]>(url);
  }

  getCrossTypesBySensorIds(sensorIds: number[]) {
    const url = `${this.sensorUrl}/cross`;
    return this.http.post<SensorValueType[]>(url, sensorIds);
  }

  getAllSensors() {
    const url = `${this.sensorUrl}`;
    return this.http.get<Sensor[]>(url);
  }

  changeSensorState(id: number) {
    const url = `${this.sensorUrl}/${id}/state`;
    return this.http.post<boolean>(url, {});
  }

  createSensor(sensorModel: any) {
    const url = `${this.sensorUrl}`;
    return this.http.put<BaseStation>(url, sensorModel);
  }

  getSensorById(sensorId: number) {
    const url = `${this.sensorUrl}/${sensorId}`;
    return this.http.get<Sensor>(url);
  }

  deleteSensor(sensorId: number) {
    const url = `${this.sensorUrl}/${sensorId}`;
    return this.http.delete<Sensor>(url);
  }
}
