import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDepartment, Department } from 'app/shared/model/department.model';
import { DepartmentService } from './department.service';
import { ICollege } from 'app/shared/model/college.model';
import { CollegeService } from 'app/entities/college/college.service';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html'
})
export class DepartmentUpdateComponent implements OnInit {
  isSaving = false;
  colleges: ICollege[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    symbol: [null, [Validators.required]],
    coverImgUrl: [],
    collegeId: []
  });

  constructor(
    protected departmentService: DepartmentService,
    protected collegeService: CollegeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.updateForm(department);

      this.collegeService.query().subscribe((res: HttpResponse<ICollege[]>) => (this.colleges = res.body || []));
    });
  }

  updateForm(department: IDepartment): void {
    this.editForm.patchValue({
      id: department.id,
      name: department.name,
      symbol: department.symbol,
      coverImgUrl: department.coverImgUrl,
      collegeId: department.collegeId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const department = this.createFromForm();
    if (department.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(department));
    }
  }

  private createFromForm(): IDepartment {
    return {
      ...new Department(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      symbol: this.editForm.get(['symbol'])!.value,
      coverImgUrl: this.editForm.get(['coverImgUrl'])!.value,
      collegeId: this.editForm.get(['collegeId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  trackById(index: number, item: ICollege): any {
    return item.id;
  }
}
