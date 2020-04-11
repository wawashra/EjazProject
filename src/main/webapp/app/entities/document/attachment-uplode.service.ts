import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttachmentUplodeService {
  private added = new Subject<any>();
  addedOb = this.added.asObservable();

  private removed = new Subject<any>();
  removedOb = this.removed.asObservable();

  private courseSymbol = new Subject<string>();
  courseSymbolOb = this.courseSymbol.asObservable();

  constructor() {}

  add(attachment: any): void {
    this.added.next(attachment);
  }

  remove(attachment: any): void {
    this.removed.next(attachment);
  }

  sendCourseSymbol(symbol: string): void {
    this.courseSymbol.next(symbol);
  }
}
