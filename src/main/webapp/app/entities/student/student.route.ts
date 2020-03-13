import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStudent, Student } from 'app/shared/model/student.model';
import { StudentService } from './student.service';
import { StudentComponent } from './student.component';
import { StudentDetailComponent } from './student-detail.component';
import { StudentUpdateComponent } from './student-update.component';

@Injectable({ providedIn: 'root' })
export class StudentResolve implements Resolve<IStudent> {
  constructor(private service: StudentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStudent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((student: HttpResponse<Student>) => {
          if (student.body) {
            return of(student.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Student());
  }
}

export const studentRoute: Routes = [
  {
    path: '',
    component: StudentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.student.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: StudentDetailComponent,
    resolve: {
      student: StudentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.student.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: StudentUpdateComponent,
    resolve: {
      student: StudentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.student.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: StudentUpdateComponent,
    resolve: {
      student: StudentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.student.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
