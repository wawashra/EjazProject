import { Directive, HostListener, Output, EventEmitter } from '@angular/core';

@Directive({
  selector: '[jhiDropzone]'
})
export class DropzoneDirective {
  @Output() dropped = new EventEmitter<FileList>();
  @Output() hovered = new EventEmitter<boolean>();

  @HostListener('drop', ['$event'])
  onDrop($event: any): void {
    $event.preventDefault();
    this.dropped.emit($event.dataTransfer.files);
    this.hovered.emit(false);
  }

  @HostListener('dragover', ['$event'])
  onDragOver($event: any): void {
    $event.preventDefault();
    this.hovered.emit(true);
  }

  @HostListener('dragleave', ['$event'])
  onDragLeave($event: any): void {
    $event.preventDefault();
    this.hovered.emit(false);
  }
}
