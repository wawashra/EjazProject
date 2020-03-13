export interface IReport {
  id?: number;
  message?: string;
  documentTitle?: string;
  documentId?: number;
  studentName?: string;
  studentId?: number;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public message?: string,
    public documentTitle?: string,
    public documentId?: number,
    public studentName?: string,
    public studentId?: number
  ) {}
}
