import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IStudent, Student } from 'app/shared/model/student.model';
import { StudentService } from './student.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IUniversity } from 'app/shared/model/university.model';
import { UniversityService } from 'app/entities/university/university.service';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department/department.service';
import { ICollege } from 'app/shared/model/college.model';
import { CollegeService } from 'app/entities/college/college.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

type SelectableEntity = IUser | IUniversity | IDepartment | ICollege | ICourse;

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html'
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  universities: IUniversity[] = [];
  departments: IDepartment[] = [];
  colleges: ICollege[] = [];
  courses: ICourse[] = [];
  birthdayDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    birthday: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    profileImgUrl: [],
    coverImgUrl: [],
    star: [],
    userId: [],
    universityId: [],
    departmentId: [],
    collegeId: [],
    courses: []
  });

  constructor(
    protected studentService: StudentService,
    protected userService: UserService,
    protected universityService: UniversityService,
    protected departmentService: DepartmentService,
    protected collegeService: CollegeService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.universityService
        .query({ 'studentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IUniversity[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IUniversity[]) => {
          if (!student.universityId) {
            this.universities = resBody;
          } else {
            this.universityService
              .find(student.universityId)
              .pipe(
                map((subRes: HttpResponse<IUniversity>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IUniversity[]) => (this.universities = concatRes));
          }
        });

      this.departmentService
        .query({ 'studentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDepartment[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IDepartment[]) => {
          if (!student.departmentId) {
            this.departments = resBody;
          } else {
            this.departmentService
              .find(student.departmentId)
              .pipe(
                map((subRes: HttpResponse<IDepartment>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDepartment[]) => (this.departments = concatRes));
          }
        });

      this.collegeService
        .query({ 'studentId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ICollege[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICollege[]) => {
          if (!student.collegeId) {
            this.colleges = resBody;
          } else {
            this.collegeService
              .find(student.collegeId)
              .pipe(
                map((subRes: HttpResponse<ICollege>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICollege[]) => (this.colleges = concatRes));
          }
        });

      this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body || []));
    });
  }

  updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      name: student.name,
      birthday: student.birthday,
      phoneNumber: student.phoneNumber,
      gender: student.gender,
      profileImgUrl: student.profileImgUrl,
      coverImgUrl: student.coverImgUrl,
      star: student.star,
      userId: student.userId,
      universityId: student.universityId,
      departmentId: student.departmentId,
      collegeId: student.collegeId,
      courses: student.courses
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  private createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      birthday: this.editForm.get(['birthday'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      profileImgUrl: this.editForm.get(['profileImgUrl'])!.value,
      coverImgUrl: this.editForm.get(['coverImgUrl'])!.value,
      star: this.editForm.get(['star'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      universityId: this.editForm.get(['universityId'])!.value,
      departmentId: this.editForm.get(['departmentId'])!.value,
      collegeId: this.editForm.get(['collegeId'])!.value,
      courses: this.editForm.get(['courses'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  getSelected(selectedVals: ICourse[], option: ICourse): ICourse {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
