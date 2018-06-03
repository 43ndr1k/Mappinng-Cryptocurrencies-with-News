import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { CryptoNewsService } from '../rest/crypto-news.service';

@Component({
  selector: 'crypto-currency-news',
  templateUrl: './crypto-currency-news.component.html',
  styleUrls: ['./crypto-currency-news.component.css']
})
export class CryptoCurrencyNewsComponent implements OnChanges {

  @Input() startDate;
  @Input() endDate;

  pagination = {};
  pages = [];
  page = 1;
  news = [];
  noResults = true;

  constructor(private route: ActivatedRoute, private router: Router, private cryptoNewsService: CryptoNewsService) {

    this.pagination = {};
    this.pages = [];
    this.news = [];

    this.route.params.subscribe(params => {

      var page = 'page' in params ? +params['page'] : undefined;
      this.page = page;

      this.loadNews(this.startDate, this.endDate, this.page);
    });
  }


  ngOnChanges() {
    this.loadNews(this.startDate, this.endDate, this.page);
  }


  convertDate(date: Date): string {

    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' +
      date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
  }

  loadNews(startDate, endDate, page?: number): void {

    var start = null;
    var end = null;

    if (startDate && endDate) {
      start = this.convertDate(startDate);
      end = this.convertDate(endDate);
    }

    this.cryptoNewsService.getNewsArticles(page, start, end).then(result => {
      this.news = result['content'];
      this.noResults = this.news.length === 0;

      var pagination = result;
      delete pagination['content'];
      this.pagination = pagination;
      this.page = pagination['number'];
    });
  }


  onPageChange(newPageNumber: number): void {

    this.router.navigateByUrl('news/' + String(newPageNumber));
  }

}
