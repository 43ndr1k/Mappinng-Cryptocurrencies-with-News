import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { RestModule } from '../rest/rest.module'
import { CryptoCurrencyNewsComponent } from './crypto-currency-news.component';
import { CryptoCurrencyNewsItemComponent } from './crypto-currency-news-item.component';

@NgModule({
  declarations: [
    CryptoCurrencyNewsComponent,
    CryptoCurrencyNewsItemComponent
  ],
  imports: [
    BrowserModule,
    RestModule,
    RouterModule,
    NgbModule.forRoot()
  ],
  exports: [
    CryptoCurrencyNewsComponent
  ],
  providers: [],
  bootstrap: []
})
export class CryptoCurrencyNewsModule { }
