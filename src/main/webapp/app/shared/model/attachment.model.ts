export interface IAttachment {
  id?: number;
  name?: string;
  url?: string;
  extension?: string;
  fileSize?: string;
  hits?: number;
  documentTitle?: string;
  documentId?: number;
  attachmentTypeType?: string;
  attachmentTypeId?: number;
}

export class Attachment implements IAttachment {
  constructor(
    public id?: number,
    public name?: string,
    public url?: string,
    public extension?: string,
    public fileSize?: string,
    public hits?: number,
    public documentTitle?: string,
    public documentId?: number,
    public attachmentTypeType?: string,
    public attachmentTypeId?: number
  ) {}
}
