import { Component, ViewEncapsulation, ChangeDetectionStrategy, Input } from '@angular/core';
import {
  trigger,
  style,
  animate,
  transition
} from '@angular/animations';

import { scaleTime } from 'd3-scale';
import { BaseChartComponent, LineChartComponent } from '@swimlane/ngx-charts';


@Component({
  selector: 'crypto-line-chart',
  templateUrl: './crypto-line-chart.component.html',
  styleUrls: ['../../../node_modules/@swimlane/ngx-charts/release/common/base-chart.component.css'],
encapsulation: ViewEncapsulation.None,
changeDetection: ChangeDetectionStrategy.OnPush,
animations: [
  trigger('animationState', [
    transition(':leave', [
      style({
        opacity: 1,
      }),
      animate(500, style({
        opacity: 0
      }))
    ])
  ])

]
})
export class CryptoLineChartComponent extends LineChartComponent {

  @Input() onUpdateDomain: Function;

  getXScale(domain, width): any {

    // TODO: adapt scale?

    let scale = scaleTime()
      .range([0, width])
      .domain(domain);

    return scale;
  }


  updateDomain(event) {

    super.updateDomain(event);
    this.onUpdateDomain(event);
  }
}
