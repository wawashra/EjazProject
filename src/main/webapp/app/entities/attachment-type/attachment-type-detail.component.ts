import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttachmentType } from 'app/shared/model/attachment-type.model';

@Component({
  selector: 'jhi-attachment-type-detail',
  templateUrl: './attachment-type-detail.component.html'
})
export class AttachmentTypeDetailComponent implements OnInit {
  attachmentType: IAttachmentType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachmentType }) => (this.attachmentType = attachmentType));
  }

  previousState(): void {
    window.history.back();
  }
}
