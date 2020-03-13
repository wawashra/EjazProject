import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICollege } from 'app/shared/model/college.model';
import { CollegeService } from './college.service';

@Component({
  templateUrl: './college-delete-dialog.component.html'
})
export class CollegeDeleteDialogComponent {
  college?: ICollege;

  constructor(protected collegeService: CollegeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.collegeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('collegeListModification');
      this.activeModal.close();
    });
  }
}
