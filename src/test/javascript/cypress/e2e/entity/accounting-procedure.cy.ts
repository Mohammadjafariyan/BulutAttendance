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

describe('AccountingProcedure e2e test', () => {
  const accountingProcedurePageUrl = '/bulutattendance/accounting-procedure';
  const accountingProcedurePageUrlPattern = new RegExp('/bulutattendance/accounting-procedure(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountingProcedureSample = {};

  let accountingProcedure;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/accounting-procedures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/accounting-procedures').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/accounting-procedures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountingProcedure) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/accounting-procedures/${accountingProcedure.id}`,
      }).then(() => {
        accountingProcedure = undefined;
      });
    }
  });

  it('AccountingProcedures menu should load AccountingProcedures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/accounting-procedure');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountingProcedure').should('exist');
    cy.url().should('match', accountingProcedurePageUrlPattern);
  });

  describe('AccountingProcedure page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountingProcedurePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountingProcedure page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/accounting-procedure/new$'));
        cy.getEntityCreateUpdateHeading('AccountingProcedure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedurePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/accounting-procedures',
          body: accountingProcedureSample,
        }).then(({ body }) => {
          accountingProcedure = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/accounting-procedures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/accounting-procedures?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/accounting-procedures?page=0&size=20>; rel="first"',
              },
              body: [accountingProcedure],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountingProcedurePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountingProcedure page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountingProcedure');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedurePageUrlPattern);
      });

      it('edit button click should load edit AccountingProcedure page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingProcedure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedurePageUrlPattern);
      });

      it('edit button click should load edit AccountingProcedure page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountingProcedure');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedurePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountingProcedure', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountingProcedure').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountingProcedurePageUrlPattern);

        accountingProcedure = undefined;
      });
    });
  });

  describe('new AccountingProcedure page', () => {
    beforeEach(() => {
      cy.visit(`${accountingProcedurePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountingProcedure');
    });

    it('should create an instance of AccountingProcedure', () => {
      cy.get(`[data-cy="title"]`).type('تلفن خام');
      cy.get(`[data-cy="title"]`).should('have.value', 'تلفن خام');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accountingProcedure = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accountingProcedurePageUrlPattern);
    });
  });
});
