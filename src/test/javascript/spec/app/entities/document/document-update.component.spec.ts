import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { DocumentUpdateComponent } from 'app/entities/document/document-update.component';
import { DocumentService } from 'app/entities/document/document.service';
import { Document } from 'app/shared/model/document.model';

describe('Component Tests', () => {
  describe('Document Management Update Component', () => {
    let comp: DocumentUpdateComponent;
    let fixture: ComponentFixture<DocumentUpdateComponent>;
    let service: DocumentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [DocumentUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DocumentService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Document(123);
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
        const entity = new Document();
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
