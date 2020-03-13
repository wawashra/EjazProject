import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IUniversity, University } from 'app/shared/model/university.model';
import { UniversityService } from './university.service';
import { UniversityComponent } from './university.component';
import { UniversityDetailComponent } from './university-detail.component';
import { UniversityUpdateComponent } from './university-update.component';

@Injectable({ providedIn: 'root' })
export class UniversityResolve implements Resolve<IUniversity> {
  constructor(private service: UniversityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUniversity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((university: HttpResponse<University>) => {
          if (university.body) {
            return of(university.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new University());
  }
}

export const universityRoute: Routes = [
  {
    path: '',
    component: UniversityComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.university.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UniversityDetailComponent,
    resolve: {
      university: UniversityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.university.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UniversityUpdateComponent,
    resolve: {
      university: UniversityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.university.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UniversityUpdateComponent,
    resolve: {
      university: UniversityResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.university.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
