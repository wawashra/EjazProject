import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocument } from 'app/shared/model/document.model';

@Component({
  selector: 'jhi-document-detail',
  templateUrl: './document-detail.component.html'
})
export class DocumentDetailComponent implements OnInit {
  document: IDocument | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => (this.document = document));
  }

  previousState(): void {
    window.history.back();
  }
}
