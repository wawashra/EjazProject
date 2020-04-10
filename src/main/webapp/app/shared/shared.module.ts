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
const config = {
  apiKey: 'AIzaSyDG_ZQdmFTVqpjJGMgtIHM13YkDq4Sqx5A',
  authDomain: 'ejaz-3cba5.firebaseapp.com',
  databaseURL: 'https://ejaz-3cba5.firebaseio.com',
  projectId: 'ejaz-3cba5',
  storageBucket: 'ejaz-3cba5.appspot.com',
  messagingSenderId: '584026588503',
  appId: '1:584026588503:web:57ec5449eebfd6b1372a12',
  measurementId: 'G-D006ZBXLZQ'
};
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
