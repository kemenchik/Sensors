import {Component, Input, OnInit} from '@angular/core';
import {ChartDataSets, ChartOptions} from 'chart.js';
import {Label} from 'ng2-charts';
import {SensorService} from '../../services/sensor.service';


@Component({
  selector: 'app-chart-line',
  templateUrl: './line.component.html',
  styleUrls: ['./line.component.css']
})
export class LineComponent implements OnInit {
  @Input() data;
  @Input() labels;
  coefficient = 0;
  typeLoading = true;
  canCoeff = false;

  types = [];
  selectedTypes = [];
  lineChartData: ChartDataSets[] = [];

  lineChartLabels: Label[] = [];

  lineChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{}],
      yAxes: [
        {
          id: 'y-axis-0',
          position: 'left',
        },
        {
          id: 'y-axis-1',
          position: 'right',
          gridLines: {
            color: 'rgba(255,0,0,0.3)',
          },
          ticks: {
            fontColor: 'red',
          }
        }
      ]
    },
  };

  lineChartLegend = true;
  lineChartPlugins = [];
  lineChartType = 'line';

  constructor(private sensorService: SensorService) {
  }

  ngOnInit(): void {
    this.lineChartData = [];
    this.lineChartLabels = [];
    this.canCoefficient(this.data);
    if (this.canCoeff) {
      const n = this.data[0][0][2].length > this.data[0][1][2].length ? this.data[0][1][2].length : this.data[0][0][2].length;
      this.correlationCoefficient(this.data[0][0][2], this.data[0][1][2], n);
    }
    this.labels.forEach(el => this.lineChartLabels.push(el));
    if (this.data) {
      const code = this.data[0][0][0].code;
      this.data[0].forEach(el => {
        const name = el[0].code + ' - ' + el[1];
        if (el[0].code === code) {
          this.lineChartData.push({data: el[2], label: name, yAxisID: 'y-axis-0'});
        } else {
          this.lineChartData.push({data: el[2], label: name, yAxisID: 'y-axis-1'});
        }
      });
    }
    this.typesInit();
  }

  typesInit() {
    this.sensorService.getCrossTypesBySensorSerial('55555').subscribe(data => {
      data.forEach(el => {
        this.types.push(el);
        this.typeLoading = false;
      });
    });
  }

  input() {
    if (this.selectedTypes.length > 2) {
      this.selectedTypes = this.selectedTypes.slice(1);
    }
  }

  canCoefficient(data) {
    let res = false;
    if (data[0].length === 2) {
      if (data[0][0][0].code !== data[0][1][0].code) {
        res = true;
      }
    }
    this.canCoeff = res;
  }

  correlationCoefficient(x: [], y: [], n: number) {
    let sumX = 0;
    let sumY = 0;
    let sumXY = 0;
    let squareSumX = 0;
    let squareSumY = 0;
    let corr = 0;
    console.log(x);
    console.log(y);
    console.log(n);

    for (let i = 0; i < n; i++) {
      sumX += x[i];
      sumY += y[i];
      sumXY = sumXY + x[i] * y[i];
      squareSumX = squareSumX + x[i] * x[i];
      squareSumY = squareSumY + y[i] * y[i];
    }

    console.log(sumX);
    console.log(sumY);
    console.log(sumXY);
    console.log(squareSumX);
    console.log(squareSumY);
    corr = (n * sumXY - sumX * sumY) / (
      Math.sqrt((n * squareSumX - sumX * sumX) * (n * squareSumY - sumY * sumY)));
    this.coefficient = corr;
  }
}
