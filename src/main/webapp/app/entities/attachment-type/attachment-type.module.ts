import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EjazSharedModule } from 'app/shared/shared.module';
import { AttachmentTypeComponent } from './attachment-type.component';
import { AttachmentTypeDetailComponent } from './attachment-type-detail.component';
import { AttachmentTypeUpdateComponent } from './attachment-type-update.component';
import { AttachmentTypeDeleteDialogComponent } from './attachment-type-delete-dialog.component';
import { attachmentTypeRoute } from './attachment-type.route';

@NgModule({
  imports: [EjazSharedModule, RouterModule.forChild(attachmentTypeRoute)],
  declarations: [
    AttachmentTypeComponent,
    AttachmentTypeDetailComponent,
    AttachmentTypeUpdateComponent,
    AttachmentTypeDeleteDialogComponent
  ],
  entryComponents: [AttachmentTypeDeleteDialogComponent]
})
export class EjazAttachmentTypeModule {}
