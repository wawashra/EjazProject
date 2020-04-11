import { IDocument } from 'app/shared/model/document.model';

export interface IDocumentType {
  id?: number;
  type?: string;
  documents?: IDocument[];
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public type?: string, public documents?: IDocument[]) {}
}
