import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttachmentType } from 'app/shared/model/attachment-type.model';
import { AttachmentTypeService } from './attachment-type.service';

@Component({
  templateUrl: './attachment-type-delete-dialog.component.html'
})
export class AttachmentTypeDeleteDialogComponent {
  attachmentType?: IAttachmentType;

  constructor(
    protected attachmentTypeService: AttachmentTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attachmentTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attachmentTypeListModification');
      this.activeModal.close();
    });
  }
}
