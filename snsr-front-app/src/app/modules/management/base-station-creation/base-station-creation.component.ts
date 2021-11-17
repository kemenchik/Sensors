import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {BaseStation} from '../../../shared/models/base.station';
import {BaseStationService} from '../../../shared/services/base.station.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-base-station-creation',
  templateUrl: './base-station-creation.component.html',
  styleUrls: ['./base-station-creation.component.css']
})
export class BaseStationCreationComponent implements OnInit {
  @Output() onStationCreation = new EventEmitter();
  stationForm: FormGroup;

  constructor(private baseStationService: BaseStationService,
              private formBuilder: FormBuilder) {
    this.stationForm = this.formBuilder.group({
        serialNumber: ['', [Validators.required, Validators.minLength(5)]],
        name: ['', [Validators.required, Validators.minLength(5)]],
        registered: ''
      }
    );
  }

  get form() { return this.stationForm.controls; }

  ngOnInit(): void {
  }

  create() {
    if (!this.stationForm.invalid) {
      this.baseStationService.createBaseStation({
        serialNumber: this.form.serialNumber.value,
        registered: this.form.registered.value,
        name: this.form.name.value
      } as BaseStation).subscribe(() => {
        this.onStationCreation.emit();
      });
      this.stationForm.reset();
    }
  }
}
