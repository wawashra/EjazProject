import { Moment } from 'moment';
import { IDocument } from 'app/shared/model/document.model';
import { IReport } from 'app/shared/model/report.model';
import { ICourse } from 'app/shared/model/course.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IStudent {
  id?: number;
  name?: string;
  birthday?: Moment;
  phoneNumber?: string;
  gender?: Gender;
  profileImgUrl?: string;
  coverImgUrl?: string;
  star?: boolean;
  userId?: number;
  universityName?: string;
  universityId?: number;
  departmentName?: string;
  departmentId?: number;
  collegeName?: string;
  collegeId?: number;
  documents?: IDocument[];
  reports?: IReport[];
  courses?: ICourse[];
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public name?: string,
    public birthday?: Moment,
    public phoneNumber?: string,
    public gender?: Gender,
    public profileImgUrl?: string,
    public coverImgUrl?: string,
    public star?: boolean,
    public userId?: number,
    public universityName?: string,
    public universityId?: number,
    public departmentName?: string,
    public departmentId?: number,
    public collegeName?: string,
    public collegeId?: number,
    public documents?: IDocument[],
    public reports?: IReport[],
    public courses?: ICourse[]
  ) {
    this.star = this.star || false;
  }
}
