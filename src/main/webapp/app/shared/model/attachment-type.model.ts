import { IAttachment } from 'app/shared/model/attachment.model';

export interface IAttachmentType {
  id?: number;
  type?: string;
  attachments?: IAttachment[];
}

export class AttachmentType implements IAttachmentType {
  constructor(public id?: number, public type?: string, public attachments?: IAttachment[]) {}
}
