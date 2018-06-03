import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { NewsArticle } from '../model/news_article';
import { Constants } from './constants';


@Injectable()
export class CurrencyService {
  constructor(private http: HttpClient) { }

  public getCurrency(symbol: string): Promise<Object> {

    let apiURL = Constants.CURRENCY_URL;
    let params = new HttpParams();
    params = params.append('shortName', String(symbol));

    let promise =  this.http.get(apiURL, { params: params })
      .toPromise()
      .catch(err => { return []; });

    return promise;
  }

  public getCryptoCurrency(symbol: string): Promise<Object> {

    let apiURL = Constants.CRYPTO_CURRENCY_URL;
    let params = new HttpParams();
    params = params.append('cryptoName', String(symbol));

    let promise =  this.http.get(apiURL, { params: params })
      .toPromise()
      .catch(err => { return []; });

    return promise;
  }

  public getWorldMapData(symbol: string): Promise<Object> {

    let apiURL = Constants.CRYPTO_WORLD_MAP_DATA;
    let params = new HttpParams();
    params = params.append('cryptoName', String(symbol));

    let promise =  this.http.get(apiURL, { params: params })
      .toPromise()
      .catch(err => { return {}; });

    return promise;
  }
}
