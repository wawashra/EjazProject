import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttachment, Attachment } from 'app/shared/model/attachment.model';
import { AttachmentService } from './attachment.service';
import { IDocument } from 'app/shared/model/document.model';
import { DocumentService } from 'app/entities/document/document.service';
import { IAttachmentType } from 'app/shared/model/attachment-type.model';
import { AttachmentTypeService } from 'app/entities/attachment-type/attachment-type.service';

type SelectableEntity = IDocument | IAttachmentType;

@Component({
  selector: 'jhi-attachment-update',
  templateUrl: './attachment-update.component.html'
})
export class AttachmentUpdateComponent implements OnInit {
  isSaving = false;
  documents: IDocument[] = [];
  attachmenttypes: IAttachmentType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    url: [null, [Validators.required]],
    extension: [null, [Validators.required]],
    fileSize: [null, [Validators.required]],
    hits: [],
    documentId: [],
    attachmentTypeId: []
  });

  constructor(
    protected attachmentService: AttachmentService,
    protected documentService: DocumentService,
    protected attachmentTypeService: AttachmentTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.updateForm(attachment);

      this.documentService.query().subscribe((res: HttpResponse<IDocument[]>) => (this.documents = res.body || []));

      this.attachmentTypeService.query().subscribe((res: HttpResponse<IAttachmentType[]>) => (this.attachmenttypes = res.body || []));
    });
  }

  updateForm(attachment: IAttachment): void {
    this.editForm.patchValue({
      id: attachment.id,
      name: attachment.name,
      url: attachment.url,
      extension: attachment.extension,
      fileSize: attachment.fileSize,
      hits: attachment.hits,
      documentId: attachment.documentId,
      attachmentTypeId: attachment.attachmentTypeId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachment = this.createFromForm();
    if (attachment.id !== undefined) {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    }
  }

  private createFromForm(): IAttachment {
    return {
      ...new Attachment(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      url: this.editForm.get(['url'])!.value,
      extension: this.editForm.get(['extension'])!.value,
      fileSize: this.editForm.get(['fileSize'])!.value,
      hits: this.editForm.get(['hits'])!.value,
      documentId: this.editForm.get(['documentId'])!.value,
      attachmentTypeId: this.editForm.get(['attachmentTypeId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachment>>): void {
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
