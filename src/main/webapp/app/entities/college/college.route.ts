import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICollege, College } from 'app/shared/model/college.model';
import { CollegeService } from './college.service';
import { CollegeComponent } from './college.component';
import { CollegeDetailComponent } from './college-detail.component';
import { CollegeUpdateComponent } from './college-update.component';

@Injectable({ providedIn: 'root' })
export class CollegeResolve implements Resolve<ICollege> {
  constructor(private service: CollegeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollege> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((college: HttpResponse<College>) => {
          if (college.body) {
            return of(college.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new College());
  }
}

export const collegeRoute: Routes = [
  {
    path: '',
    component: CollegeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.college.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CollegeDetailComponent,
    resolve: {
      college: CollegeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.college.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CollegeUpdateComponent,
    resolve: {
      college: CollegeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.college.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CollegeUpdateComponent,
    resolve: {
      college: CollegeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.college.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
