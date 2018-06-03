import { Component, ViewEncapsulation } from '@angular/core';

import {NgxChartsModule} from '@swimlane/ngx-charts';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  title = 'app';

  view: any[] = [700, 400];
}
