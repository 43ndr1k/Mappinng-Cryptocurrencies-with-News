import { Component, ViewEncapsulation, Input } from '@angular/core';

import * as moment from 'moment';
import { cloneDeep } from 'lodash';

import { CurrencyService } from '../rest/currency.service';

@Component({
  selector: 'crypto-prices',
  templateUrl: './crypto-prices.component.html',
  styleUrls: ['./crypto-prices.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CryptoPricesComponent {

  @Input() onUpdateDomain: Function;

  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'time';
  showYAxisLabel = true;
  yAxisLabel = 'USD';

  colorScheme = 'cool';

  autoScale = true;

  title = 'app';

  currencies = [
    {
      'name': 'Euro',
      'symbol': 'eur'
    },
  ];

  cryptoCurrencies = [
    {
      'name': 'Bitcoin',
      'symbol': 'btc'
    },
    {
      'name': 'Ethereum',
      'symbol': 'eth'
    },
    {
      'name': 'Dash',
      'symbol': 'dsh'
    },
    {
      'name': 'Litecoin',
      'symbol': 'ltc'
    },
  ];

  data: any[];
  dataView: any[];

  useLogScale: boolean = false;

  constructor(private currencyService: CurrencyService) {

    // original data
    this.data = [];

    // copy of the data
    this.dataView = [];

    this.reloadCryptoData();
  }

  reloadCryptoData() {

    var promiseList = []

    for (let cryptoCurrency of this.cryptoCurrencies) {

      var promise = this.currencyService.getCryptoCurrency(cryptoCurrency.symbol);
      promiseList.push(promise);
    }

    for (let currency of this.currencies) {

      var promise = this.currencyService.getCurrency(currency.symbol);
      promiseList.push(promise);
    }

    Promise.all(promiseList)
      .then(value => {
        this.processData(value);
        this.transformData();
      }, err => {
        console.log(err);
      });
  }


  processData(promiseResults) {

    this.data = [];


    for (let dto of promiseResults) {

      if (dto != null) {

        for (let item of dto) {

          if ('description' in item) {

            // crypto currency dto
            this.processCryptoData(item);
          } else {

            // currency dto
            this.processCurrencyData(item);
          }
        }
      }
    }


  }


  transformData() {

    this.dataView = [];
    for (let symbolData of this.data) {

      let symbolDataCopy = cloneDeep(symbolData);

      if (this.useLogScale) {

        symbolDataCopy['series'] = symbolDataCopy['series'].map(timeSeriesValue => {

          timeSeriesValue['value'] = Math.log(timeSeriesValue['value']+ 1.0);

          return timeSeriesValue;
        });
      }

      this.dataView.push(symbolDataCopy);
    }
  }


  processCryptoData(cryptoCurrencyDto) {

    var name = cryptoCurrencyDto['shortName'];

    if (!Object.keys(this.data).find(k => this.data[k].name === name)) {

      // usd is assumed right now
      if (cryptoCurrencyDto['cryptoCurrencyPrices'][0]['unit'] !== 'usd') {

        throw Error('Currency not supported: ' + cryptoCurrencyDto['unit']);
      }

      var newSymbolData = {};
      newSymbolData['name'] = cryptoCurrencyDto['description']['longName'];

      // TODO: fix this once we have a reasonable dto
      newSymbolData['series'] = cryptoCurrencyDto['cryptoCurrencyPrices'][0]['timeSeriesValues'];
      newSymbolData['series'] = this.renameValues(newSymbolData['series']);

      this.data.push(newSymbolData);
    }
  }


  processCurrencyData(currencyDto) {

    var newSymbolData = {};
    newSymbolData['name'] = currencyDto['shortName'].toUpperCase();

    // TODO: fix this once we have a reasonable dto
    newSymbolData['series'] = currencyDto['timeSeriesValues'];
    newSymbolData['series'] = this.renameValues(newSymbolData['series']);

    this.data.push(newSymbolData);
  }


  renameValues(values) {

    for (var i = 0; i < values.length; i++) {
      values[i]['name'] = moment(values[i]['date']).toDate();
      delete values[i]['date'];
    }

    return values;
  }


  onSelect(event) {

    // do nothing
  }


  updateDomain(event) {

    this.onUpdateDomain(event);
  }

  onLogscaleChange() {

    this.transformData();

    if (this.useLogScale) {

      this.yAxisLabel = 'log USD';
    } else {

      this.yAxisLabel = 'USD';
    }
  }


  xAxisTickFormatter(val) {

    var date = new Date(val);

    var minutes = date.getMinutes();
    var hours = date.getHours();

    var month = date.getMonth() + 1;
    var day = date.getDate();

    if (day === 1 || day - 1 % 10 === 0) {

      return month + '/' + day + '/' + date.getFullYear();

    }

    return month + '/' + day + '/' + date.getFullYear();
  }
}
