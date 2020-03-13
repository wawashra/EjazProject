import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttachment } from 'app/shared/model/attachment.model';

type EntityResponseType = HttpResponse<IAttachment>;
type EntityArrayResponseType = HttpResponse<IAttachment[]>;

@Injectable({ providedIn: 'root' })
export class AttachmentService {
  public resourceUrl = SERVER_API_URL + 'api/attachments';

  constructor(protected http: HttpClient) {}

  create(attachment: IAttachment): Observable<EntityResponseType> {
    return this.http.post<IAttachment>(this.resourceUrl, attachment, { observe: 'response' });
  }

  update(attachment: IAttachment): Observable<EntityResponseType> {
    return this.http.put<IAttachment>(this.resourceUrl, attachment, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAttachment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAttachment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
