import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NgxChartsModule, LineChartModule } from '@swimlane/ngx-charts';

import { RestModule } from '../rest/rest.module'

import { CryptoPricesComponent } from './crypto-prices.components';
import { CryptoLineChartComponent } from './crypto-line-chart.component';

@NgModule({
  declarations: [
    CryptoPricesComponent,
    CryptoLineChartComponent
  ],
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    LineChartModule,
    NgxChartsModule,
    RestModule
  ],
  exports: [
    CryptoPricesComponent,
    CryptoLineChartComponent
  ],
  providers: [],
  bootstrap: []
})
export class CryptoPricesModule { }
