import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccProccParameterDetailComponent } from './acc-procc-parameter-detail.component';

describe('AccProccParameter Management Detail Component', () => {
  let comp: AccProccParameterDetailComponent;
  let fixture: ComponentFixture<AccProccParameterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccProccParameterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccProccParameterDetailComponent,
              resolve: { accProccParameter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccProccParameterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccProccParameterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accProccParameter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccProccParameterDetailComponent);

      // THEN
      expect(instance.accProccParameter()).toEqual(expect.objectContaining({ id: 123 }));
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
