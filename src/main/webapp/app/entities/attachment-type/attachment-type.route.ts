import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttachmentType, AttachmentType } from 'app/shared/model/attachment-type.model';
import { AttachmentTypeService } from './attachment-type.service';
import { AttachmentTypeComponent } from './attachment-type.component';
import { AttachmentTypeDetailComponent } from './attachment-type-detail.component';
import { AttachmentTypeUpdateComponent } from './attachment-type-update.component';

@Injectable({ providedIn: 'root' })
export class AttachmentTypeResolve implements Resolve<IAttachmentType> {
  constructor(private service: AttachmentTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttachmentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attachmentType: HttpResponse<AttachmentType>) => {
          if (attachmentType.body) {
            return of(attachmentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AttachmentType());
  }
}

export const attachmentTypeRoute: Routes = [
  {
    path: '',
    component: AttachmentTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.attachmentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AttachmentTypeDetailComponent,
    resolve: {
      attachmentType: AttachmentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.attachmentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AttachmentTypeUpdateComponent,
    resolve: {
      attachmentType: AttachmentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.attachmentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AttachmentTypeUpdateComponent,
    resolve: {
      attachmentType: AttachmentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.attachmentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
