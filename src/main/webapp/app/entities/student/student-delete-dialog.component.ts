import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from './student.service';

@Component({
  templateUrl: './student-delete-dialog.component.html'
})
export class StudentDeleteDialogComponent {
  student?: IStudent;

  constructor(protected studentService: StudentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.studentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('studentListModification');
      this.activeModal.close();
    });
  }
}
