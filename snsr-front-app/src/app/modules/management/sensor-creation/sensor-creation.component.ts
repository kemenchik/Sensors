import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {SensorService} from '../../../shared/services/sensor.service';
import {Sensor} from '../../../shared/models/sensor';
import {BaseStation} from '../../../shared/models/base.station';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BaseStationService} from '../../../shared/services/base.station.service';

@Component({
  selector: 'app-sensor-creation',
  templateUrl: './sensor-creation.component.html',
  styleUrls: ['./sensor-creation.component.css']
})
export class SensorCreationComponent implements OnInit {
  @Output() onSensorCreation = new EventEmitter();
  @Input () stations;
  sensorForm: FormGroup;

  constructor(private sensorService: SensorService,
              private formBuilder: FormBuilder) {
    this.sensorForm = this.formBuilder.group({
        serialNumber: ['', [Validators.required, Validators.minLength(5)]],
        registered: '',
        name: ['', [Validators.required, Validators.minLength(5)]],
        stationSerialNumber: ['', Validators.required]
      }
    );
  }

  get form() { return this.sensorForm.controls; }

  ngOnInit(): void {
  }

  create() {
    if (!this.sensorForm.invalid) {
      this.sensorService.createSensor({
          sensor: new Sensor(this.form.name.value, this.form.serialNumber.value, this.form.registered.value),
          stationSerialNumber: this.form.stationSerialNumber.value
        }
      ).subscribe((value => {
        this.onSensorCreation.emit();
      }));
      this.sensorForm.reset();
    }
  }
}
