import { Component } from '@angular/core';

@Component({
  selector: 'jhi-uploader',
  templateUrl: './uploader.component.html',
  styleUrls: ['./uploader.component.scss']
})
export class UploaderComponent {
  isHovering?: boolean;

  files: any[] = [];

  toggleHover(event: boolean): void {
    this.isHovering = event;
  }

  onDrop(files: FileList): void {
    for (let i = 0; i < files.length; i++) {
      this.files.push(files[i]);
    }
  }
}
