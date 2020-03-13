import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EjazTestModule } from '../../../test.module';
import { UniversityDetailComponent } from 'app/entities/university/university-detail.component';
import { University } from 'app/shared/model/university.model';

describe('Component Tests', () => {
  describe('University Management Detail Component', () => {
    let comp: UniversityDetailComponent;
    let fixture: ComponentFixture<UniversityDetailComponent>;
    const route = ({ data: of({ university: new University(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EjazTestModule],
        declarations: [UniversityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UniversityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UniversityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load university on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.university).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
