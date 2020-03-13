import { ICourse } from 'app/shared/model/course.model';

export interface IDepartment {
  id?: number;
  name?: string;
  symbol?: string;
  coverImgUrl?: string;
  courses?: ICourse[];
  collegeSymbol?: string;
  collegeId?: number;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string,
    public symbol?: string,
    public coverImgUrl?: string,
    public courses?: ICourse[],
    public collegeSymbol?: string,
    public collegeId?: number
  ) {}
}
