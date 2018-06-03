import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';

import { PagesModule } from './pages/pages.module';
import { NewsPageComponent } from './pages/news-page.component';
import { WorldMapPageComponent } from './pages/world-map-page.component';

const routes: Routes = [
  { path: '', redirectTo: 'news/1', pathMatch: 'full' },
  { path: 'news', component: NewsPageComponent },
  { path: 'news/:page', component: NewsPageComponent },
  { path: 'map', component: WorldMapPageComponent }
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    PagesModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
