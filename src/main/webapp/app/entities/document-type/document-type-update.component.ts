import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDocumentType, DocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from './document-type.service';

@Component({
  selector: 'jhi-document-type-update',
  templateUrl: './document-type-update.component.html'
})
export class DocumentTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]]
  });

  constructor(protected documentTypeService: DocumentTypeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentType }) => {
      this.updateForm(documentType);
    });
  }

  updateForm(documentType: IDocumentType): void {
    this.editForm.patchValue({
      id: documentType.id,
      type: documentType.type
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentType = this.createFromForm();
    if (documentType.id !== undefined) {
      this.subscribeToSaveResponse(this.documentTypeService.update(documentType));
    } else {
      this.subscribeToSaveResponse(this.documentTypeService.create(documentType));
    }
  }

  private createFromForm(): IDocumentType {
    return {
      ...new DocumentType(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentType>>): void {
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
