import { Component, OnInit, Input } from '@angular/core';
import { AngularFireStorage, AngularFireUploadTask } from '@angular/fire/storage';
import { AngularFirestore } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { finalize, tap } from 'rxjs/operators';
import { AttachmentUplodeService } from 'app/entities/document/attachment-uplode.service';

@Component({
  selector: 'jhi-upload-task',
  templateUrl: './upload-task.component.html',
  styleUrls: ['./upload-task.component.scss']
})
export class UploadTaskComponent implements OnInit {
  @Input() file?: any;
  @Input() courseCode?: string;

  show = true;
  task?: AngularFireUploadTask;
  percentage?: Observable<number | any>;
  snapshot?: Observable<any>;
  downloadURL?: string;
  ref?: any;
  constructor(private storage: AngularFireStorage, private db: AngularFirestore, private attachmentUplode: AttachmentUplodeService) {}

  ngOnInit(): void {
    this.startUpload();
  }

  startUpload(): any {
    // The storage path
    if (this.file) {
      alert(`
      ${this.file.name}
      ${this.file.path}
      ${this.file.size}
      ${this.file.type}
      `);
      const path = `ejaz/${this.courseCode}/${Date.now()}_${this.file.name}`;

      // Reference to storage bucket
      this.ref = this.storage.ref(path);

      // The main task
      this.task = this.ref.put(this.file, { customMetadata: { blah: 'blah' } });
      if (this.task) {
        // Progress monitoring
        this.percentage = this.task.percentageChanges()!;

        this.snapshot = this.task.snapshotChanges().pipe(
          tap(),
          // The file's download URL
          finalize(() => {
            this.ref.getDownloadURL().subscribe((fileUrl: string) => {
              this.downloadURL = fileUrl;
              this.attachmentUplode.add({
                name: this.file.name,
                fileSize: this.file.size,
                url: fileUrl,
                attachmentTypeId: 0,
                fileType: this.file.type
              });
            });
          })
        );
      }
    }
  }

  isActive(snapshot: any): any {
    return snapshot.state === 'running' && snapshot.bytesTransferred < snapshot.totalBytes;
  }

  remove(): void {
    this.ref.delete().subscribe(() => {
      if (this.file) {
        alert(`تم حذف  الملف ${this.file.name}بنجاح`);
        this.attachmentUplode.remove(this.file.name);
      }
    });
    this.show = false;
  }

  cancelUplode(): void {
    if (this.task) {
      this.task.cancel();
      this.show = false;
    }
  }
}
