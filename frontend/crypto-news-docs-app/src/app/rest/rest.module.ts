import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';

import { ModelModule } from '../model/model.module';

import { CurrencyService } from './currency.service';
import { CryptoNewsService } from './crypto-news.service';


@NgModule({
  declarations: [],
  imports: [
    HttpClientModule
  ],
  providers: [
    CurrencyService,
    CryptoNewsService
  ],
  bootstrap: []
})
export class RestModule { }
