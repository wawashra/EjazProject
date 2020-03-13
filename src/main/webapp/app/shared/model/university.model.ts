import { ICollege } from 'app/shared/model/college.model';

export interface IUniversity {
  id?: number;
  name?: string;
  symbol?: string;
  colleges?: ICollege[];
}

export class University implements IUniversity {
  constructor(public id?: number, public name?: string, public symbol?: string, public colleges?: ICollege[]) {}
}
