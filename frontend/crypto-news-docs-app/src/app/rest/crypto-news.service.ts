import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { NewsArticle } from '../model/news_article';

import { Constants } from './constants';

@Injectable()
export class CryptoNewsService {
  constructor(private http: HttpClient) { }

  public getNewsArticles(page?: number, startDate?: string, endDate?: string): Promise<Object> {

    let apiURL = Constants.CRYPTO_CURRENCY_NEWS_URL;
    let params = new HttpParams();

    if (startDate && endDate) {

      apiURL = Constants.CRYPTO_CURRENCY_NEWS_BY_DATE_URL;
      params = params.append('startDate', startDate);
      params = params.append('endDate', endDate);
    }

    if (page !== undefined) {

      params = params.append('page', String(page));
    }

    return this.http.get(apiURL, { params: params }).toPromise();
  }
}
