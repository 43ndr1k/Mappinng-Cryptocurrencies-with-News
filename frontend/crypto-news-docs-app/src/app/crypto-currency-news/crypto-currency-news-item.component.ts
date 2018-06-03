import { Component, Input, ElementRef, ViewChild, AfterViewInit, OnChanges } from '@angular/core';

@Component({
  selector: 'crypto-currency-news-item',
  templateUrl: './crypto-currency-news-item.component.html',
  styleUrls: ['./crypto-currency-news-item.component.css']
})
export class CryptoCurrencyNewsItemComponent implements AfterViewInit, OnChanges {

  @ViewChild('anchor') anchorElement: ElementRef;
  @Input() item;

  origin: String;
  keywords: string[];

  ngOnChanges() {

    if (this.item && this.item.metaInfo.keywords) {

      this.keywords = this.item.metaInfo.keywords.split(',');
    }
  }

  ngAfterViewInit() {

    var self = this;

    // setTimeout is required here to avoid ExpressionChangedAfterItHasBeenCheckedError:
    // https://github.com/angular/angular/issues/17572
    setTimeout(function() {
      self.origin = self.anchorElement.nativeElement.origin + '/';
    });
  }
}
