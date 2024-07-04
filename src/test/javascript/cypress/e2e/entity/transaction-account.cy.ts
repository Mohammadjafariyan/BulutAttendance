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

describe('TransactionAccount e2e test', () => {
  const transactionAccountPageUrl = '/bulutattendance/transaction-account';
  const transactionAccountPageUrlPattern = new RegExp('/bulutattendance/transaction-account(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const transactionAccountSample = {};

  let transactionAccount;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/transaction-accounts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/transaction-accounts').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/transaction-accounts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transactionAccount) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/transaction-accounts/${transactionAccount.id}`,
      }).then(() => {
        transactionAccount = undefined;
      });
    }
  });

  it('TransactionAccounts menu should load TransactionAccounts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/transaction-account');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccount').should('exist');
    cy.url().should('match', transactionAccountPageUrlPattern);
  });

  describe('TransactionAccount page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionAccountPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionAccount page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/transaction-account/new$'));
        cy.getEntityCreateUpdateHeading('TransactionAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/transaction-accounts',
          body: transactionAccountSample,
        }).then(({ body }) => {
          transactionAccount = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/transaction-accounts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/transaction-accounts?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/transaction-accounts?page=0&size=20>; rel="first"',
              },
              body: [transactionAccount],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionAccountPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TransactionAccount page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccount');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });

      it('edit button click should load edit TransactionAccount page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccount');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });

      it('edit button click should load edit TransactionAccount page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionAccount');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);
      });

      it('last delete button click should delete instance of TransactionAccount', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionAccount').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountPageUrlPattern);

        transactionAccount = undefined;
      });
    });
  });

  describe('new TransactionAccount page', () => {
    beforeEach(() => {
      cy.visit(`${transactionAccountPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TransactionAccount');
    });

    it('should create an instance of TransactionAccount', () => {
      cy.get(`[data-cy="debitAmount"]`).type('26332.32');
      cy.get(`[data-cy="debitAmount"]`).should('have.value', '26332.32');

      cy.get(`[data-cy="creditAmount"]`).type('11581.17');
      cy.get(`[data-cy="creditAmount"]`).should('have.value', '11581.17');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        transactionAccount = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', transactionAccountPageUrlPattern);
    });
  });
});
