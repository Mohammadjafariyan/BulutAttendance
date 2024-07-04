import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountingProcedureDetailComponent } from './accounting-procedure-detail.component';

describe('AccountingProcedure Management Detail Component', () => {
  let comp: AccountingProcedureDetailComponent;
  let fixture: ComponentFixture<AccountingProcedureDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountingProcedureDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccountingProcedureDetailComponent,
              resolve: { accountingProcedure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccountingProcedureDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountingProcedureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountingProcedure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccountingProcedureDetailComponent);

      // THEN
      expect(instance.accountingProcedure()).toEqual(expect.objectContaining({ id: 123 }));
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
