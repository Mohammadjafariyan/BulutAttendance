import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { HrLetterDetailComponent } from './hr-letter-detail.component';

describe('HrLetter Management Detail Component', () => {
  let comp: HrLetterDetailComponent;
  let fixture: ComponentFixture<HrLetterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HrLetterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HrLetterDetailComponent,
              resolve: { hrLetter: () => of({ id: '9fec3727-3421-4967-b213-ba36557ca194' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HrLetterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HrLetterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hrLetter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HrLetterDetailComponent);

      // THEN
      expect(instance.hrLetter()).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
