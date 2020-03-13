import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { UniversityUpdateComponent } from 'app/entities/university/university-update.component';
import { UniversityService } from 'app/entities/university/university.service';
import { University } from 'app/shared/model/university.model';

describe('Component Tests', () => {
  describe('University Management Update Component', () => {
    let comp: UniversityUpdateComponent;
    let fixture: ComponentFixture<UniversityUpdateComponent>;
    let service: UniversityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [UniversityUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UniversityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UniversityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UniversityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new University(123);
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
        const entity = new University();
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
