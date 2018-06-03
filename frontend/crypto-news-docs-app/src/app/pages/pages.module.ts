import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { CryptoCurrencyNewsModule } from '../crypto-currency-news/crypto-currency-news.module';
import { CryptoPricesModule } from '../crypto-currency-charts/crypto-prices.module';
import { WorldMapModule } from '../world-map/world-map.module';

import { NewsPageComponent } from './news-page.component';
import { WorldMapPageComponent } from './world-map-page.component';

@NgModule({
  declarations: [
    NewsPageComponent,
    WorldMapPageComponent
  ],
  imports: [
    BrowserModule,
    CryptoCurrencyNewsModule,
    CryptoPricesModule,
    WorldMapModule
  ],
  exports: [
    NewsPageComponent,
    WorldMapPageComponent
  ],
  providers: [],
  bootstrap: []
})
export class PagesModule { }
