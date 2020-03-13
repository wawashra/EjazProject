import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICollege, College } from 'app/shared/model/college.model';
import { CollegeService } from './college.service';
import { IUniversity } from 'app/shared/model/university.model';
import { UniversityService } from 'app/entities/university/university.service';

@Component({
  selector: 'jhi-college-update',
  templateUrl: './college-update.component.html'
})
export class CollegeUpdateComponent implements OnInit {
  isSaving = false;
  universities: IUniversity[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    symbol: [null, [Validators.required]],
    coverImgUrl: [],
    universityId: []
  });

  constructor(
    protected collegeService: CollegeService,
    protected universityService: UniversityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ college }) => {
      this.updateForm(college);

      this.universityService.query().subscribe((res: HttpResponse<IUniversity[]>) => (this.universities = res.body || []));
    });
  }

  updateForm(college: ICollege): void {
    this.editForm.patchValue({
      id: college.id,
      name: college.name,
      symbol: college.symbol,
      coverImgUrl: college.coverImgUrl,
      universityId: college.universityId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const college = this.createFromForm();
    if (college.id !== undefined) {
      this.subscribeToSaveResponse(this.collegeService.update(college));
    } else {
      this.subscribeToSaveResponse(this.collegeService.create(college));
    }
  }

  private createFromForm(): ICollege {
    return {
      ...new College(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      symbol: this.editForm.get(['symbol'])!.value,
      coverImgUrl: this.editForm.get(['coverImgUrl'])!.value,
      universityId: this.editForm.get(['universityId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollege>>): void {
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

  trackById(index: number, item: IUniversity): any {
    return item.id;
  }
}
