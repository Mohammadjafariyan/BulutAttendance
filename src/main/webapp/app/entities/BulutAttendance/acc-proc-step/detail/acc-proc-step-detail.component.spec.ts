import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccProcStepDetailComponent } from './acc-proc-step-detail.component';

describe('AccProcStep Management Detail Component', () => {
  let comp: AccProcStepDetailComponent;
  let fixture: ComponentFixture<AccProcStepDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccProcStepDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccProcStepDetailComponent,
              resolve: { accProcStep: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccProcStepDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccProcStepDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accProcStep on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccProcStepDetailComponent);

      // THEN
      expect(instance.accProcStep()).toEqual(expect.objectContaining({ id: 123 }));
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
