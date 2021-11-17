import {Sensor} from './sensor';

export class BaseStation {
  id: number;
  serialNumber: string;
  name: string;
  registered: boolean;
  sensors: Sensor[];
}
