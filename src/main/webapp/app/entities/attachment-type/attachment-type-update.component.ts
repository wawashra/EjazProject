import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAttachmentType, AttachmentType } from 'app/shared/model/attachment-type.model';
import { AttachmentTypeService } from './attachment-type.service';

@Component({
  selector: 'jhi-attachment-type-update',
  templateUrl: './attachment-type-update.component.html'
})
export class AttachmentTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]]
  });

  constructor(protected attachmentTypeService: AttachmentTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachmentType }) => {
      this.updateForm(attachmentType);
    });
  }

  updateForm(attachmentType: IAttachmentType): void {
    this.editForm.patchValue({
      id: attachmentType.id,
      type: attachmentType.type
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachmentType = this.createFromForm();
    if (attachmentType.id !== undefined) {
      this.subscribeToSaveResponse(this.attachmentTypeService.update(attachmentType));
    } else {
      this.subscribeToSaveResponse(this.attachmentTypeService.create(attachmentType));
    }
  }

  private createFromForm(): IAttachmentType {
    return {
      ...new AttachmentType(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachmentType>>): void {
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
}
