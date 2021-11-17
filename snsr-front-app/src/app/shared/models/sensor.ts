import {SensorValue} from './sensor.value';

export class Sensor {
  id: number;
  serialNumber: string;
  name: string;
  registered: boolean;
  values?: SensorValue[] = [];

  constructor(name: string, serialNumber: string, registered: boolean) {
    this.name = name;
    this.registered = registered;
    this.serialNumber = serialNumber;
  }
}
