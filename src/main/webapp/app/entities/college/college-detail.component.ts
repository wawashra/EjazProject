import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollege } from 'app/shared/model/college.model';

@Component({
  selector: 'jhi-college-detail',
  templateUrl: './college-detail.component.html'
})
export class CollegeDetailComponent implements OnInit {
  college: ICollege | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ college }) => (this.college = college));
  }

  previousState(): void {
    window.history.back();
  }
}
