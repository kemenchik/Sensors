import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {BaseStation} from '../models/base.station';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BaseStationService {

  private stationUrl = `${environment.apiUrl}/station`;

  constructor(private http: HttpClient) {
  }

  getAllBaseStations() {
    return this.http.get<BaseStation[]>(this.stationUrl);
  }

  getAllActiveBaseStations() {
    const url = `${this.stationUrl}/active`;
    return this.http.get<BaseStation[]>(url);
  }

  changeStationState(id: number) {
    const url = `${this.stationUrl}/${id}/state`;
    return this.http.post<boolean>(url, {});
  }

  createBaseStation(station: BaseStation) {
    const url = `${this.stationUrl}`;
    return this.http.put<BaseStation>(url, station);
  }

  getBaseStationById(stationId: number) {
    const url = `${this.stationUrl}/${stationId}`;
    return this.http.get<BaseStation>(url);
  }

  deleteStation(stationId: number) {
    const url = `${this.stationUrl}/${stationId}`;
    return this.http.delete<boolean>(url);
  }
}
