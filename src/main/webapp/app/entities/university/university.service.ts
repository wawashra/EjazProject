import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUniversity } from 'app/shared/model/university.model';

type EntityResponseType = HttpResponse<IUniversity>;
type EntityArrayResponseType = HttpResponse<IUniversity[]>;

@Injectable({ providedIn: 'root' })
export class UniversityService {
  public resourceUrl = SERVER_API_URL + 'api/universities';

  constructor(protected http: HttpClient) {}

  create(university: IUniversity): Observable<EntityResponseType> {
    return this.http.post<IUniversity>(this.resourceUrl, university, { observe: 'response' });
  }

  update(university: IUniversity): Observable<EntityResponseType> {
    return this.http.put<IUniversity>(this.resourceUrl, university, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUniversity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUniversity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
