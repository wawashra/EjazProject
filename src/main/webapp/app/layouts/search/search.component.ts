import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { CourseService } from 'app/entities/course/course.service';
import { HttpResponse } from '@angular/common/http';
import { ICourse } from 'app/shared/model/course.model';
import { Observable } from 'rxjs';
@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  filteredStates?: Observable<ICourse[]>;
  stateCtrl = new FormControl();
  states: ICourse[] = [];
  course = '';

  constructor(protected courseService: CourseService) {}

  ngOnInit(): void {
    this.stateCtrl.valueChanges.subscribe(newValue => {
      this.filterICourse(newValue);
    });
  }

  private filterICourse(value: string): void {
    const filterValue = value.toLowerCase();
    this.courseService
      .query({
        'name.contains': filterValue
      })
      .subscribe((res: HttpResponse<ICourse[]>) => {
        this.states = res.body || [];
        this.filteredStates = new Observable<[]>();
      });
  }
}
