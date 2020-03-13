import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IReport, Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { IDocument } from 'app/shared/model/document.model';
import { DocumentService } from 'app/entities/document/document.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

type SelectableEntity = IDocument | IStudent;

@Component({
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html'
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;
  documents: IDocument[] = [];
  students: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    message: [null, [Validators.required]],
    documentId: [],
    studentId: []
  });

  constructor(
    protected reportService: ReportService,
    protected documentService: DocumentService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ report }) => {
      this.updateForm(report);

      this.documentService.query().subscribe((res: HttpResponse<IDocument[]>) => (this.documents = res.body || []));

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(report: IReport): void {
    this.editForm.patchValue({
      id: report.id,
      message: report.message,
      documentId: report.documentId,
      studentId: report.studentId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const report = this.createFromForm();
    if (report.id !== undefined) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  private createFromForm(): IReport {
    return {
      ...new Report(),
      id: this.editForm.get(['id'])!.value,
      message: this.editForm.get(['message'])!.value,
      documentId: this.editForm.get(['documentId'])!.value,
      studentId: this.editForm.get(['studentId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
