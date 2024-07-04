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

describe('AccountingProcedureExecution e2e test', () => {
  const accountingProcedureExecutionPageUrl = '/bulutattendance/accounting-procedure-execution';
  const accountingProcedureExecutionPageUrlPattern = new RegExp('/bulutattendance/accounting-procedure-execution(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountingProcedureExecutionSample = {};

  let accountingProcedureExecution;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/accounting-procedure-executions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/accounting-procedure-executions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/accounting-procedure-executions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountingProcedureExecution) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/accounting-procedure-executions/${accountingProcedureExecution.id}`,
      }).then(() => {
        accountingProcedureExecution = undefined;
      });
    }
  });

  it('AccountingProcedureExecutions menu should load AccountingProcedureExecutions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/accounting-procedure-execution');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountingProcedureExecution').should('exist');
    cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
  });

  describe('AccountingProcedureExecution page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountingProcedureExecutionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountingProcedureExecution page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/accounting-procedure-execution/new$'));
        cy.getEntityCreateUpdateHeading('AccountingProcedureExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/accounting-procedure-executions',
          body: accountingProcedureExecutionSample,
        }).then(({ body }) => {
          accountingProcedureExecution = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/accounting-procedure-executions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/accounting-procedure-executions?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/accounting-procedure-executions?page=0&size=20>; rel="first"',
              },
              body: [accountingProcedureExecution],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountingProcedureExecutionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountingProcedureExecution page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountingProcedureExecution');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
      });

      it('edit button click should load edit AccountingProcedureExecution page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingProcedureExecution');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
      });

      it('edit button click should load edit AccountingProcedureExecution page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingProcedureExecution');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
      });

      it('last delete button click should delete instance of AccountingProcedureExecution', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountingProcedureExecution').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedureExecutionPageUrlPattern);

        accountingProcedureExecution = undefined;
      });
    });
  });

  describe('new AccountingProcedureExecution page', () => {
    beforeEach(() => {
      cy.visit(`${accountingProcedureExecutionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountingProcedureExecution');
    });

    it('should create an instance of AccountingProcedureExecution', () => {
      cy.get(`[data-cy="dateTime"]`).type('2024-06-09T18:55');
      cy.get(`[data-cy="dateTime"]`).blur();
      cy.get(`[data-cy="dateTime"]`).should('have.value', '2024-06-09T18:55');

      cy.get(`[data-cy="desc"]`).type('دانشگاه');
      cy.get(`[data-cy="desc"]`).should('have.value', 'دانشگاه');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accountingProcedureExecution = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accountingProcedureExecutionPageUrlPattern);
    });
  });
});
