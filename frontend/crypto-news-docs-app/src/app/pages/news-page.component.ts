import { throttle } from 'lodash';

import { Component } from '@angular/core';

@Component({
  selector: 'news-page',
  templateUrl: './news-page.component.html'
})
export class NewsPageComponent {

  dateRange = {};

  constructor() {

    this.dateRange['startDate'] = '';
    this.dateRange['endDate'] = '';
  }

  updateDomain = (event) => {
    var self = this;
    throttle(function() {

      self.dateRange['startDate'] = event[0];
      self.dateRange['endDate'] = event[1];
    }, 400, {trailing: true, leading: false})();

  };
}
