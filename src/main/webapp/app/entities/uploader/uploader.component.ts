import { Component, OnInit } from '@angular/core';
import { AttachmentUplodeService } from 'app/entities/document/attachment-uplode.service';

@Component({
  selector: 'jhi-uploader',
  templateUrl: './uploader.component.html',
  styleUrls: ['./uploader.component.scss']
})
export class UploaderComponent implements OnInit {
  isHovering?: boolean;
  files: any[] = [];
  courseCode?: string;
  constructor(private attachmentUploded: AttachmentUplodeService) {}

  ngOnInit(): void {
    this.attachmentUploded.courseSymbolOb.subscribe((symp: string) => {
      this.courseCode = symp;
    });
  }

  toggleHover(event: boolean): void {
    this.isHovering = event;
  }

  onDrop(files: FileList): void {
    for (let i = 0; i < files.length; i++) {
      alert(`
      onDrop
      ${files.item(i).name}
      ${files.item(i).path}
      ${files.item(i).size}
      ${files.item(i).type}
      `);
      this.files.push(files.item(i));
    }
  }
}
