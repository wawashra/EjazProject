import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { CourseComponent } from './course.component';
import { CourseDetailComponent } from './course-detail.component';
import { CourseUpdateComponent } from './course-update.component';

@Injectable({ providedIn: 'root' })
export class CourseResolve implements Resolve<ICourse> {
  constructor(private service: CourseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((course: HttpResponse<Course>) => {
          if (course.body) {
            return of(course.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Course());
  }
}

export const courseRoute: Routes = [
  {
    path: '',
    component: CourseComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.course.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CourseDetailComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.course.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.course.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ejazApp.course.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
