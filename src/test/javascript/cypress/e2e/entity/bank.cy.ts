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

describe('Bank e2e test', () => {
  const bankPageUrl = '/bulutattendance/bank';
  const bankPageUrlPattern = new RegExp('/bulutattendance/bank(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bankSample = {};

  let bank;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/banks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/banks').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/banks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bank) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/banks/${bank.id}`,
      }).then(() => {
        bank = undefined;
      });
    }
  });

  it('Banks menu should load Banks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/bank');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Bank').should('exist');
    cy.url().should('match', bankPageUrlPattern);
  });

  describe('Bank page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Bank page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/bank/new$'));
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/banks',
          body: bankSample,
        }).then(({ body }) => {
          bank = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/banks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/banks?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/banks?page=0&size=20>; rel="first"',
              },
              body: [bank],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Bank page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bank');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('edit button click should load edit Bank page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('edit button click should load edit Bank page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bank');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);
      });

      it('last delete button click should delete instance of Bank', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bank').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bankPageUrlPattern);

        bank = undefined;
      });
    });
  });

  describe('new Bank page', () => {
    beforeEach(() => {
      cy.visit(`${bankPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Bank');
    });

    it('should create an instance of Bank', () => {
      cy.get(`[data-cy="title"]`).type('یا');
      cy.get(`[data-cy="title"]`).should('have.value', 'یا');

      cy.get(`[data-cy="code"]`).type('سفارش دادن');
      cy.get(`[data-cy="code"]`).should('have.value', 'سفارش دادن');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bank = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', bankPageUrlPattern);
    });
  });
});
