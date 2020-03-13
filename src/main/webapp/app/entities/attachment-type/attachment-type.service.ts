import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttachmentType } from 'app/shared/model/attachment-type.model';

type EntityResponseType = HttpResponse<IAttachmentType>;
type EntityArrayResponseType = HttpResponse<IAttachmentType[]>;

@Injectable({ providedIn: 'root' })
export class AttachmentTypeService {
  public resourceUrl = SERVER_API_URL + 'api/attachment-types';

  constructor(protected http: HttpClient) {}

  create(attachmentType: IAttachmentType): Observable<EntityResponseType> {
    return this.http.post<IAttachmentType>(this.resourceUrl, attachmentType, { observe: 'response' });
  }

  update(attachmentType: IAttachmentType): Observable<EntityResponseType> {
    return this.http.put<IAttachmentType>(this.resourceUrl, attachmentType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttachmentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttachmentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
