import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUniversity } from 'app/shared/model/university.model';

@Component({
  selector: 'jhi-university-detail',
  templateUrl: './university-detail.component.html'
})
export class UniversityDetailComponent implements OnInit {
  university: IUniversity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ university }) => (this.university = university));
  }

  previousState(): void {
    window.history.back();
  }
}
