import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { ReportUpdateComponent } from 'app/entities/report/report-update.component';
import { ReportService } from 'app/entities/report/report.service';
import { Report } from 'app/shared/model/report.model';

describe('Component Tests', () => {
  describe('Report Management Update Component', () => {
    let comp: ReportUpdateComponent;
    let fixture: ComponentFixture<ReportUpdateComponent>;
    let service: ReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [ReportUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReportUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReportService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Report(123);
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
        const entity = new Report();
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
