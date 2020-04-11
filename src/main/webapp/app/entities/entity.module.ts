import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'university',
        loadChildren: () => import('./university/university.module').then(m => m.EjazUniversityModule)
      },
      {
        path: 'college',
        loadChildren: () => import('./college/college.module').then(m => m.EjazCollegeModule)
      },
      {
        path: 'department',
        loadChildren: () => import('./department/department.module').then(m => m.EjazDepartmentModule)
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.EjazCourseModule)
      },
      {
        path: 'document',
        loadChildren: () => import('./document/document.module').then(m => m.EjazDocumentModule)
      },
      {
        path: 'attachment-type',
        loadChildren: () => import('./attachment-type/attachment-type.module').then(m => m.EjazAttachmentTypeModule)
      },
      {
        path: 'attachment',
        loadChildren: () => import('./attachment/attachment.module').then(m => m.EjazAttachmentModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.EjazTagModule)
      },
      {
        path: 'report',
        loadChildren: () => import('./report/report.module').then(m => m.EjazReportModule)
      },
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.EjazStudentModule)
      },
      {
        path: 'document-type',
        loadChildren: () => import('./document-type/document-type.module').then(m => m.EjazDocumentTypeModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  exports: [],
  declarations: []
})
export class EjazEntityModule {}
