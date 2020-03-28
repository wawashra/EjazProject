import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EjazSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { FooterComponent } from 'app/layouts/footer/footer.component';

@NgModule({
  imports: [EjazSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, FooterComponent]
})
export class EjazHomeModule {}
