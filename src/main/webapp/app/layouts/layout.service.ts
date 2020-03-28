import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {
  private expanded = new Subject<boolean>();
  isExpanded = this.expanded.asObservable();
  constructor() {}

  changeExpanded(expanded: boolean): void {
    this.expanded.next(expanded);
  }
}
