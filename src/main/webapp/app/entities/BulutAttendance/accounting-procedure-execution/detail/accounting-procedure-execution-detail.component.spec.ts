import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountingProcedureExecutionDetailComponent } from './accounting-procedure-execution-detail.component';

describe('AccountingProcedureExecution Management Detail Component', () => {
  let comp: AccountingProcedureExecutionDetailComponent;
  let fixture: ComponentFixture<AccountingProcedureExecutionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountingProcedureExecutionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccountingProcedureExecutionDetailComponent,
              resolve: { accountingProcedureExecution: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccountingProcedureExecutionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountingProcedureExecutionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountingProcedureExecution on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccountingProcedureExecutionDetailComponent);

      // THEN
      expect(instance.accountingProcedureExecution()).toEqual(expect.objectContaining({ id: 123 }));
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
