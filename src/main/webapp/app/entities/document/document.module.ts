import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EjazSharedModule } from 'app/shared/shared.module';
import { DocumentComponent } from './document.component';
import { DocumentDetailComponent } from './document-detail.component';
import { DocumentDeleteDialogComponent } from './document-delete-dialog.component';
import { documentRoute } from './document.route';

@NgModule({
  imports: [EjazSharedModule, RouterModule.forChild(documentRoute)],
  declarations: [DocumentComponent, DocumentDetailComponent, DocumentDeleteDialogComponent],
  entryComponents: [DocumentDeleteDialogComponent]
})
export class EjazDocumentModule {}
