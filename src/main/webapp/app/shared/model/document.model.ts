import { IAttachment } from 'app/shared/model/attachment.model';
import { IReport } from 'app/shared/model/report.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IDocument {
  id?: number;
  title?: string;
  active?: boolean;
  description?: string;
  ratingSum?: number;
  ratingNumber?: number;
  view?: number;
  attachments?: IAttachment[];
  reports?: IReport[];
  tags?: ITag[];
  courseSymbol?: string;
  courseId?: number;
  studentName?: string;
  studentId?: number;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public title?: string,
    public active?: boolean,
    public description?: string,
    public ratingSum?: number,
    public ratingNumber?: number,
    public view?: number,
    public attachments?: IAttachment[],
    public reports?: IReport[],
    public tags?: ITag[],
    public courseSymbol?: string,
    public courseId?: number,
    public studentName?: string,
    public studentId?: number
  ) {
    this.active = this.active || false;
  }
}
