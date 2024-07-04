jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PersonnelStatusService } from '../service/personnel-status.service';

import { PersonnelStatusDeleteDialogComponent } from './personnel-status-delete-dialog.component';

describe('PersonnelStatus Management Delete Component', () => {
  let comp: PersonnelStatusDeleteDialogComponent;
  let fixture: ComponentFixture<PersonnelStatusDeleteDialogComponent>;
  let service: PersonnelStatusService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PersonnelStatusDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PersonnelStatusDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonnelStatusDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PersonnelStatusService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('9fec3727-3421-4967-b213-ba36557ca194');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
