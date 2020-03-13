import { IDocument } from 'app/shared/model/document.model';
import { IStudent } from 'app/shared/model/student.model';

export interface ICourse {
  id?: number;
  name?: string;
  symbol?: string;
  description?: string;
  coverImgUrl?: string;
  documents?: IDocument[];
  departmentSymbol?: string;
  departmentId?: number;
  students?: IStudent[];
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string,
    public symbol?: string,
    public description?: string,
    public coverImgUrl?: string,
    public documents?: IDocument[],
    public departmentSymbol?: string,
    public departmentId?: number,
    public students?: IStudent[]
  ) {}
}
