import { Component, AfterContentInit } from '@angular/core';
import * as Datamap from 'datamaps';
import { scaleLinear} from 'd3-scale';
import { select } from 'd3-selection';
import { legendColor } from 'd3-svg-legend'

import { CurrencyService } from '../rest/currency.service';


@Component({
  selector: 'world-map',
  templateUrl: './world-map.component.html',
  styleUrls: ['./world-map.component.css']
})
export class WorldMapComponent implements AfterContentInit {

  map = null;
  dataset = null;
  unit : string = null;
  paletteScale = null;

  constructor(private currencyService: CurrencyService) {}

  ngAfterContentInit() {

    let context = this;

    this.currencyService.getWorldMapData('btc')
      .then(results => {
        var data = results['data'];

        var series = [];
        for (let key of Object.keys(data)) {

          series.push([key, data[key]]);
        }

        var onlyValues = series.map(function(obj){ return obj[1]; });

        var minValue = Math.min.apply(null, onlyValues);
        var maxValue = Math.max.apply(null, onlyValues);

        this.paletteScale = scaleLinear<string>()
            .domain([minValue,maxValue])
            .range(['#EFEFFF', '#336699']);

        context.dataset = {};
        series.forEach(function(item){
          var iso = item[0],
              value = item[1];
          context.dataset[iso] = { value: value, fillColor: context.paletteScale(value) };
        });

        this.unit = results['unit'];
        this.map = this.createMap(context, this.dataset);

        var svg = select('svg');
        svg.append('g')
          .attr('class', 'legendLinear')
          .attr('transform', 'translate(20,20)')
          .attr('style', 'margin-bottom:40px');

        var legendLinear = legendColor()
          .shapeWidth(100)
          .orient('horizontal')
          .scale(this.paletteScale);

        svg.select('.legendLinear')
          .call(legendLinear);

      });

  }


  fixData(data) {

    for (let key of Object.keys(data)) {

      data[key] = parseInt(data[key]);
    }

    return data;
  }


  createMap(context, data) {

    var map = new Datamap({
      scope: 'world',
      height: null,
      width: null,
      responsive: false,
      element: document.getElementById('world-map'),
      fills: {
        defaultFill: 'rgba(200,200, 200, 0.9)'
      },
      data: data,
      geographyConfig: {
        popupTemplate: function(geo, data) {

          return '<div class="hoverinfo"><strong>'
            + geo.properties.name  +
            ': ' + new String(Math.round(data.value)) + ' [USD]'
            + '</strong></div>';
        }
      },
      done: function(datamap) {

        datamap.svg.selectAll('.datamaps-subunit').on('click', function(geography) {

          context.onClick(geography);
        });
      }
    });

    return map;
  }

  onChange(event) {

    this.currencyService.getWorldMapData('btc').then(value => this.map.updateChloropleth(value));
  }


  onClick(geography) {

    console.log(geography);
  }


}
