import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IUniversity, University } from 'app/shared/model/university.model';
import { UniversityService } from './university.service';

@Component({
  selector: 'jhi-university-update',
  templateUrl: './university-update.component.html'
})
export class UniversityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    symbol: [null, [Validators.required]]
  });

  constructor(protected universityService: UniversityService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ university }) => {
      this.updateForm(university);
    });
  }

  updateForm(university: IUniversity): void {
    this.editForm.patchValue({
      id: university.id,
      name: university.name,
      symbol: university.symbol
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const university = this.createFromForm();
    if (university.id !== undefined) {
      this.subscribeToSaveResponse(this.universityService.update(university));
    } else {
      this.subscribeToSaveResponse(this.universityService.create(university));
    }
  }

  private createFromForm(): IUniversity {
    return {
      ...new University(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      symbol: this.editForm.get(['symbol'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUniversity>>): void {
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
