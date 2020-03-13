import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocument } from 'app/shared/model/document.model';
import { DocumentService } from './document.service';

@Component({
  templateUrl: './document-delete-dialog.component.html'
})
export class DocumentDeleteDialogComponent {
  document?: IDocument;

  constructor(protected documentService: DocumentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('documentListModification');
      this.activeModal.close();
    });
  }
}
