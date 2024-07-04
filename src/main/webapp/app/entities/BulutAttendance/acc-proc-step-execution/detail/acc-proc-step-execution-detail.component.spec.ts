import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccProcStepExecutionDetailComponent } from './acc-proc-step-execution-detail.component';

describe('AccProcStepExecution Management Detail Component', () => {
  let comp: AccProcStepExecutionDetailComponent;
  let fixture: ComponentFixture<AccProcStepExecutionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccProcStepExecutionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccProcStepExecutionDetailComponent,
              resolve: { accProcStepExecution: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccProcStepExecutionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccProcStepExecutionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accProcStepExecution on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccProcStepExecutionDetailComponent);

      // THEN
      expect(instance.accProcStepExecution()).toEqual(expect.objectContaining({ id: 123 }));
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
