import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDocumentType, DocumentType } from 'app/shared/model/document-type.model';
import { DocumentTypeService } from './document-type.service';
import { DocumentTypeComponent } from './document-type.component';
import { DocumentTypeDetailComponent } from './document-type-detail.component';
import { DocumentTypeUpdateComponent } from './document-type-update.component';

@Injectable({ providedIn: 'root' })
export class DocumentTypeResolve implements Resolve<IDocumentType> {
  constructor(private service: DocumentTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((documentType: HttpResponse<DocumentType>) => {
          if (documentType.body) {
            return of(documentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentType());
  }
}

export const documentTypeRoute: Routes = [
  {
    path: '',
    component: DocumentTypeComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.documentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocumentTypeDetailComponent,
    resolve: {
      documentType: DocumentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.documentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocumentTypeUpdateComponent,
    resolve: {
      documentType: DocumentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.documentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocumentTypeUpdateComponent,
    resolve: {
      documentType: DocumentTypeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.documentType.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
