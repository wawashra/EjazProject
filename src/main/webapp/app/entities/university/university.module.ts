import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EjazSharedModule } from 'app/shared/shared.module';
import { UniversityComponent } from './university.component';
import { UniversityDetailComponent } from './university-detail.component';
import { UniversityUpdateComponent } from './university-update.component';
import { UniversityDeleteDialogComponent } from './university-delete-dialog.component';
import { universityRoute } from './university.route';

@NgModule({
  imports: [EjazSharedModule, RouterModule.forChild(universityRoute)],
  declarations: [UniversityComponent, UniversityDetailComponent, UniversityUpdateComponent, UniversityDeleteDialogComponent],
  entryComponents: [UniversityDeleteDialogComponent]
})
export class EjazUniversityModule {}
