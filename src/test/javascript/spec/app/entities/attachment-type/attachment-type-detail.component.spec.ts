import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { AttachmentTypeDetailComponent } from 'app/entities/attachment-type/attachment-type-detail.component';
import { AttachmentType } from 'app/shared/model/attachment-type.model';

describe('Component Tests', () => {
  describe('AttachmentType Management Detail Component', () => {
    let comp: AttachmentTypeDetailComponent;
    let fixture: ComponentFixture<AttachmentTypeDetailComponent>;
    const route = ({ data: of({ attachmentType: new AttachmentType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [AttachmentTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AttachmentTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttachmentTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attachmentType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attachmentType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
