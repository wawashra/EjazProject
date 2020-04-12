import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDocument, Document } from 'app/shared/model/document.model';
import { DocumentService } from './document.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag/tag.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';
import { AttachmentUplodeService } from 'app/entities/document/attachment-uplode.service';
import { AttachmentService } from 'app/entities/attachment/attachment.service';
import { Attachment, IAttachment } from 'app/shared/model/attachment.model';

type SelectableEntity = ITag | ICourse | IStudent;

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html'
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;
  tags: ITag[] = [];
  courses: ICourse[] = [];
  students: IStudent[] = [];
  courseId?: string;

  // attachments?: IAttachmentِ[] = [];
  attachments?: Map<string, any>;
  addedAttachments: IAttachment[] = [];
  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    active: [],
    description: [],
    ratingSum: [],
    ratingNumber: [],
    view: [],
    tags: [],
    courseId: [],
    studentId: []
  });

  constructor(
    protected documentService: DocumentService,
    protected tagService: TagService,
    protected courseService: CourseService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private attachmentUploded: AttachmentUplodeService,
    private attachmentService: AttachmentService
  ) {}

  ngOnInit(): void {
    this.attachments = new Map<string, any>();
    this.courseId = this.activatedRoute.snapshot.paramMap.get('cid') || '';
    this.attachmentUploded.addedOb.subscribe((att: any) => {
      this.attachments!.set(att.name, att);
    });

    this.attachmentUploded.removedOb.subscribe(name => {
      this.attachments!.delete(name);
    });

    this.activatedRoute.data.subscribe(({ document }) => {
      this.updateForm(document);

      const fil = {};

      if (this.courseId) {
        fil['id.equals'] = this.courseId;
      }

      this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => (this.tags = res.body || []));

      this.courseService.query(fil).subscribe((res: HttpResponse<ICourse[]>) => {
        this.courses = res.body || [];
        if (this.courses[0].symbol != null) {
          // alert("fff>>>>"+this.courses[0].symbol);

          this.attachmentUploded.sendCourseSymbol(this.courses[0].symbol);
        }
      });

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(document: IDocument): void {
    this.editForm.patchValue({
      id: document.id,
      title: document.title,
      active: document.active,
      description: document.description,
      ratingSum: document.ratingSum,
      ratingNumber: document.ratingNumber,
      view: document.view,
      tags: document.tags,
      courseId: document.courseId,
      studentId: document.studentId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.createFromForm();

    if (document.id !== undefined) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  private createFromForm(): IDocument {
    return {
      ...new Document(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      active: this.editForm.get(['active'])!.value,
      description: this.editForm.get(['description'])!.value,
      ratingSum: this.editForm.get(['ratingSum'])!.value,
      ratingNumber: this.editForm.get(['ratingNumber'])!.value,
      view: this.editForm.get(['view'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      courseId: this.editForm.get(['courseId'])!.value || this.courseId,
      studentId: this.editForm.get(['studentId'])!.value,
      attachments: this.getAttachment()
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
    result.subscribe(
      () => {
        this.onSaveSuccess();
      },
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

  getSelected(selectedVals: ITag[], option: ITag): ITag {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }

  getAttachment(): IAttachment[] {
    this.attachments!.forEach((value: any, key: string) => {
      this.addedAttachments.push({
        ...new Attachment(),
        name: key,
        url: value.url,
        extension: value.fileType,
        fileSize: value.fileSize,
        hits: 0,
        attachmentTypeId: 1
      });
    });
    return this.addedAttachments;
  }
}
