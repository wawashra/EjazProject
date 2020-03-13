import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { AttachmentTypeUpdateComponent } from 'app/entities/attachment-type/attachment-type-update.component';
import { AttachmentTypeService } from 'app/entities/attachment-type/attachment-type.service';
import { AttachmentType } from 'app/shared/model/attachment-type.model';

describe('Component Tests', () => {
  describe('AttachmentType Management Update Component', () => {
    let comp: AttachmentTypeUpdateComponent;
    let fixture: ComponentFixture<AttachmentTypeUpdateComponent>;
    let service: AttachmentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [AttachmentTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AttachmentTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttachmentTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttachmentTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AttachmentType(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AttachmentType();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
