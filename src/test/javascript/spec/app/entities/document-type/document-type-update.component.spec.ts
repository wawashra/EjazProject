import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { DocumentTypeUpdateComponent } from 'app/entities/document-type/document-type-update.component';
import { DocumentTypeService } from 'app/entities/document-type/document-type.service';
import { DocumentType } from 'app/shared/model/document-type.model';

describe('Component Tests', () => {
  describe('DocumentType Management Update Component', () => {
    let comp: DocumentTypeUpdateComponent;
    let fixture: ComponentFixture<DocumentTypeUpdateComponent>;
    let service: DocumentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [DocumentTypeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocumentTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocumentTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DocumentType(123);
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
        const entity = new DocumentType();
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
