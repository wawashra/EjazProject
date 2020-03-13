import { IDepartment } from 'app/shared/model/department.model';

export interface ICollege {
  id?: number;
  name?: string;
  symbol?: string;
  coverImgUrl?: string;
  departments?: IDepartment[];
  universitySymbol?: string;
  universityId?: number;
}

export class College implements ICollege {
  constructor(
    public id?: number,
    public name?: string,
    public symbol?: string,
    public coverImgUrl?: string,
    public departments?: IDepartment[],
    public universitySymbol?: string,
    public universityId?: number
  ) {}
}
