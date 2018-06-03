import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { WorldMapComponent } from './world-map.components';
import { RestModule } from '../rest/rest.module'

@NgModule({
  declarations: [
    WorldMapComponent
  ],
  imports: [
    BrowserModule,
    RestModule
  ],
  exports: [
    WorldMapComponent
  ],
  providers: [],
  bootstrap: []
})
export class WorldMapModule { }
