import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDocument, Document } from 'app/shared/model/document.model';
import { DocumentService } from './document.service';
import { DocumentComponent } from './document.component';
import { DocumentDetailComponent } from './document-detail.component';
import { DocumentUpdateComponent } from './document-update.component';

@Injectable({ providedIn: 'root' })
export class DocumentResolve implements Resolve<IDocument> {
  constructor(private service: DocumentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocument> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((document: HttpResponse<Document>) => {
          if (document.body) {
            return of(document.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Document());
  }
}

export const documentRoute: Routes = [
  {
    path: '',
    component: DocumentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'ejazApp.document.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DocumentDetailComponent,
    resolve: {
      document: DocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.document.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DocumentUpdateComponent,
    resolve: {
      document: DocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.document.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DocumentUpdateComponent,
    resolve: {
      document: DocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ejazApp.document.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
