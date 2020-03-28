import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CourseService } from 'app/entities/course/course.service';
import { HttpResponse } from '@angular/common/http';
import { ICourse } from 'app/shared/model/course.model';

export interface State {
  flag: string;
  name: string;
  population: string;
}

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  totalItems = 0;
  stateCtrl = new FormControl();
  filteredStates?: Observable<ICourse[]>;
  courses?: ICourse[];
  itemsPerPage = 10;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  states: ICourse[] = [];

  constructor(protected courseService: CourseService) {}

  private was(value: string): ICourse[] {
    this.courseService
      .query({
        'name.contains': value,
        'symbol.contains': value
      })
      .subscribe((res: HttpResponse<ICourse[]>) => {
        this.states = res.body || [];
        return this.states;
      });
    return [];
  }

  ngOnInit(): void {
    this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => {
      this.states = res.body || [];

      this.filteredStates = this.stateCtrl.valueChanges.pipe(map(state => (state ? this._filterStates(state) : this.states.slice())));
    });
  }

  private _filterStates(value: string): ICourse[] {
    this.courseService
      .query({
        'name.contains': value,
        'symbol.contains': value
      })
      .subscribe((res: HttpResponse<ICourse[]>) => {
        return res.body;
      });
    return [];
  }
}
