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

describe('AccProcStepExecution e2e test', () => {
  const accProcStepExecutionPageUrl = '/bulutattendance/acc-proc-step-execution';
  const accProcStepExecutionPageUrlPattern = new RegExp('/bulutattendance/acc-proc-step-execution(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accProcStepExecutionSample = {};

  let accProcStepExecution;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/acc-proc-step-executions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/acc-proc-step-executions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/acc-proc-step-executions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accProcStepExecution) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/acc-proc-step-executions/${accProcStepExecution.id}`,
      }).then(() => {
        accProcStepExecution = undefined;
      });
    }
  });

  it('AccProcStepExecutions menu should load AccProcStepExecutions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/acc-proc-step-execution');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccProcStepExecution').should('exist');
    cy.url().should('match', accProcStepExecutionPageUrlPattern);
  });

  describe('AccProcStepExecution page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accProcStepExecutionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccProcStepExecution page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/acc-proc-step-execution/new$'));
        cy.getEntityCreateUpdateHeading('AccProcStepExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepExecutionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/acc-proc-step-executions',
          body: accProcStepExecutionSample,
        }).then(({ body }) => {
          accProcStepExecution = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/acc-proc-step-executions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/acc-proc-step-executions?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/acc-proc-step-executions?page=0&size=20>; rel="first"',
              },
              body: [accProcStepExecution],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accProcStepExecutionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccProcStepExecution page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accProcStepExecution');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepExecutionPageUrlPattern);
      });

      it('edit button click should load edit AccProcStepExecution page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProcStepExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepExecutionPageUrlPattern);
      });

      it('edit button click should load edit AccProcStepExecution page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProcStepExecution');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepExecutionPageUrlPattern);
      });

      it('last delete button click should delete instance of AccProcStepExecution', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accProcStepExecution').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProcStepExecutionPageUrlPattern);

        accProcStepExecution = undefined;
      });
    });
  });

  describe('new AccProcStepExecution page', () => {
    beforeEach(() => {
      cy.visit(`${accProcStepExecutionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccProcStepExecution');
    });

    it('should create an instance of AccProcStepExecution', () => {
      cy.get(`[data-cy="debitAmount"]`).type('12704.65');
      cy.get(`[data-cy="debitAmount"]`).should('have.value', '12704.65');

      cy.get(`[data-cy="creditAmount"]`).type('17943.67');
      cy.get(`[data-cy="creditAmount"]`).should('have.value', '17943.67');

      cy.get(`[data-cy="desc"]`).type('پشت');
      cy.get(`[data-cy="desc"]`).should('have.value', 'پشت');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accProcStepExecution = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accProcStepExecutionPageUrlPattern);
    });
  });
});
