import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DiffMatchPatchModule } from 'ng-diff-match-patch';

import { EjazSharedModule } from 'app/shared/shared.module';
import { entityAuditRoute } from './entity-audit-routing.module';
import { EntityAuditComponent } from './entity-audit.component';
import { EntityAuditModalComponent } from './entity-audit-modal.component';

@NgModule({
  imports: [CommonModule, EjazSharedModule, DiffMatchPatchModule, RouterModule.forChild([entityAuditRoute])],
  declarations: [EntityAuditComponent, EntityAuditModalComponent],
  // https://ng-bootstrap.github.io/#/components/modal/examples
  entryComponents: [EntityAuditModalComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityAuditModule {}
