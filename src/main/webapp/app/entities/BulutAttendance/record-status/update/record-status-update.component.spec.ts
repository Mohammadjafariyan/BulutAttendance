import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { RecordStatusService } from '../service/record-status.service';
import { IRecordStatus } from '../record-status.model';
import { RecordStatusFormService } from './record-status-form.service';

import { RecordStatusUpdateComponent } from './record-status-update.component';

describe('RecordStatus Management Update Component', () => {
  let comp: RecordStatusUpdateComponent;
  let fixture: ComponentFixture<RecordStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recordStatusFormService: RecordStatusFormService;
  let recordStatusService: RecordStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RecordStatusUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RecordStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecordStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recordStatusFormService = TestBed.inject(RecordStatusFormService);
    recordStatusService = TestBed.inject(RecordStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const recordStatus: IRecordStatus = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ recordStatus });
      comp.ngOnInit();

      expect(comp.recordStatus).toEqual(recordStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecordStatus>>();
      const recordStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(recordStatusFormService, 'getRecordStatus').mockReturnValue(recordStatus);
      jest.spyOn(recordStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recordStatus }));
      saveSubject.complete();

      // THEN
      expect(recordStatusFormService.getRecordStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recordStatusService.update).toHaveBeenCalledWith(expect.objectContaining(recordStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecordStatus>>();
      const recordStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(recordStatusFormService, 'getRecordStatus').mockReturnValue({ id: null });
      jest.spyOn(recordStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recordStatus }));
      saveSubject.complete();

      // THEN
      expect(recordStatusFormService.getRecordStatus).toHaveBeenCalled();
      expect(recordStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecordStatus>>();
      const recordStatus = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(recordStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recordStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
