import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrgPositionDetailComponent } from './org-position-detail.component';

describe('OrgPosition Management Detail Component', () => {
  let comp: OrgPositionDetailComponent;
  let fixture: ComponentFixture<OrgPositionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrgPositionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrgPositionDetailComponent,
              resolve: { orgPosition: () => of({ id: '9fec3727-3421-4967-b213-ba36557ca194' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OrgPositionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrgPositionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orgPosition on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrgPositionDetailComponent);

      // THEN
      expect(instance.orgPosition()).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
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
