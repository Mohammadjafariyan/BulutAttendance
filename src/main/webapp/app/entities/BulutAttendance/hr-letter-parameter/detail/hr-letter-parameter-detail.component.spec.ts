import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { HrLetterParameterDetailComponent } from './hr-letter-parameter-detail.component';

describe('HrLetterParameter Management Detail Component', () => {
  let comp: HrLetterParameterDetailComponent;
  let fixture: ComponentFixture<HrLetterParameterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HrLetterParameterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HrLetterParameterDetailComponent,
              resolve: { hrLetterParameter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HrLetterParameterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HrLetterParameterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hrLetterParameter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HrLetterParameterDetailComponent);

      // THEN
      expect(instance.hrLetterParameter()).toEqual(expect.objectContaining({ id: 123 }));
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
