import { NgModule } from '@angular/core';
import { EjazSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { SearchComponent } from 'app/layouts/search/search.component';
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireAuthModule } from '@angular/fire/auth';
import { AngularFireStorageModule } from '@angular/fire/storage';
import { DocumentUpdateComponent } from 'app/entities/document/document-update.component';
import { UploadTaskComponent } from 'app/entities/upload-task/upload-task.component';
import { UploaderComponent } from 'app/entities/uploader/uploader.component';
import { DropzoneDirective } from 'app/dropzone.directive';
const config = {};
@NgModule({
  imports: [
    EjazSharedLibsModule,
    AngularFireModule.initializeApp(config),
    AngularFirestoreModule,
    AngularFireAuthModule,
    AngularFireStorageModule
  ],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    SearchComponent,
    DocumentUpdateComponent,
    UploaderComponent,
    UploadTaskComponent,
    DropzoneDirective
  ],
  entryComponents: [LoginModalComponent],
  exports: [
    EjazSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    SearchComponent,
    UploaderComponent,
    UploadTaskComponent
  ]
})
export class EjazSharedModule {}
