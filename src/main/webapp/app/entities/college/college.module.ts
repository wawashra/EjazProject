import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EjazSharedModule } from 'app/shared/shared.module';
import { CollegeComponent } from './college.component';
import { CollegeDetailComponent } from './college-detail.component';
import { CollegeUpdateComponent } from './college-update.component';
import { CollegeDeleteDialogComponent } from './college-delete-dialog.component';
import { collegeRoute } from './college.route';

@NgModule({
  imports: [EjazSharedModule, RouterModule.forChild(collegeRoute)],
  declarations: [CollegeComponent, CollegeDetailComponent, CollegeUpdateComponent, CollegeDeleteDialogComponent],
  entryComponents: [CollegeDeleteDialogComponent]
})
export class EjazCollegeModule {}
