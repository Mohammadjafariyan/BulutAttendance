import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('AccProcStep e2e test', () => {
  const accProcStepPageUrl = '/bulutattendance/acc-proc-step';
  const accProcStepPageUrlPattern = new RegExp('/bulutattendance/acc-proc-step(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accProcStepSample = {};

  let accProcStep;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/acc-proc-steps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/acc-proc-steps').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/acc-proc-steps/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accProcStep) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/acc-proc-steps/${accProcStep.id}`,
      }).then(() => {
        accProcStep = undefined;
      });
    }
  });

  it('AccProcSteps menu should load AccProcSteps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/acc-proc-step');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccProcStep').should('exist');
    cy.url().should('match', accProcStepPageUrlPattern);
  });

  describe('AccProcStep page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accProcStepPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccProcStep page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/acc-proc-step/new$'));
        cy.getEntityCreateUpdateHeading('AccProcStep');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/acc-proc-steps',
          body: accProcStepSample,
        }).then(({ body }) => {
          accProcStep = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/acc-proc-steps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/acc-proc-steps?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/acc-proc-steps?page=0&size=20>; rel="first"',
              },
              body: [accProcStep],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accProcStepPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccProcStep page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accProcStep');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepPageUrlPattern);
      });

      it('edit button click should load edit AccProcStep page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProcStep');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepPageUrlPattern);
      });

      it('edit button click should load edit AccProcStep page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProcStep');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepPageUrlPattern);
      });

      it('last delete button click should delete instance of AccProcStep', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accProcStep').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepPageUrlPattern);

        accProcStep = undefined;
      });
    });
  });

  describe('new AccProcStep page', () => {
    beforeEach(() => {
      cy.visit(`${accProcStepPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccProcStep');
    });

    it('should create an instance of AccProcStep', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accProcStep = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accProcStepPageUrlPattern);
    });
  });
});
