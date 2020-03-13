import { IDocument } from 'app/shared/model/document.model';

export interface ITag {
  id?: number;
  name?: string;
  documents?: IDocument[];
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public documents?: IDocument[]) {}
}
