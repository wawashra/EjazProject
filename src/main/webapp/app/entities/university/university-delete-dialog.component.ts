import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUniversity } from 'app/shared/model/university.model';
import { UniversityService } from './university.service';

@Component({
  templateUrl: './university-delete-dialog.component.html'
})
export class UniversityDeleteDialogComponent {
  university?: IUniversity;

  constructor(
    protected universityService: UniversityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.universityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('universityListModification');
      this.activeModal.close();
    });
  }
}
