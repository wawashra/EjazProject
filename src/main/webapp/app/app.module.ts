import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { EjazSharedModule } from 'app/shared/shared.module';
import { EjazCoreModule } from 'app/core/core.module';
import { EjazAppRoutingModule } from './app-routing.module';
import { EjazHomeModule } from './home/home.module';
import { EjazEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
@NgModule({
  imports: [
    BrowserModule,
    EjazSharedModule,
    EjazCoreModule,
    EjazHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    EjazEntityModule,
    EjazAppRoutingModule,
    BrowserAnimationsModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, SidebarComponent],
  bootstrap: [MainComponent]
})
export class EjazAppModule {}
