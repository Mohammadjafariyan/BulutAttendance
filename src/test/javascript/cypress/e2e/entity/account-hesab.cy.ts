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

describe('AccountHesab e2e test', () => {
  const accountHesabPageUrl = '/bulutattendance/account-hesab';
  const accountHesabPageUrlPattern = new RegExp('/bulutattendance/account-hesab(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountHesabSample = {};

  let accountHesab;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/account-hesabs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/account-hesabs').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/account-hesabs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountHesab) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/account-hesabs/${accountHesab.id}`,
      }).then(() => {
        accountHesab = undefined;
      });
    }
  });

  it('AccountHesabs menu should load AccountHesabs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/account-hesab');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountHesab').should('exist');
    cy.url().should('match', accountHesabPageUrlPattern);
  });

  describe('AccountHesab page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountHesabPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountHesab page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/account-hesab/new$'));
        cy.getEntityCreateUpdateHeading('AccountHesab');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountHesabPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/account-hesabs',
          body: accountHesabSample,
        }).then(({ body }) => {
          accountHesab = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/account-hesabs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/account-hesabs?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/account-hesabs?page=0&size=20>; rel="first"',
              },
              body: [accountHesab],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountHesabPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountHesab page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountHesab');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountHesabPageUrlPattern);
      });

      it('edit button click should load edit AccountHesab page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountHesab');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountHesabPageUrlPattern);
      });

      it('edit button click should load edit AccountHesab page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountHesab');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountHesabPageUrlPattern);
      });

      it('last delete button click should delete instance of AccountHesab', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountHesab').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountHesabPageUrlPattern);

        accountHesab = undefined;
      });
    });
  });

  describe('new AccountHesab page', () => {
    beforeEach(() => {
      cy.visit(`${accountHesabPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountHesab');
    });

    it('should create an instance of AccountHesab', () => {
      cy.get(`[data-cy="title"]`).type('شاعر دریافت کردن تازه');
      cy.get(`[data-cy="title"]`).should('have.value', 'شاعر دریافت کردن تازه');

      cy.get(`[data-cy="code"]`).type('میان');
      cy.get(`[data-cy="code"]`).should('have.value', 'میان');

      cy.get(`[data-cy="level"]`).type('5075');
      cy.get(`[data-cy="level"]`).should('have.value', '5075');

      cy.get(`[data-cy="levelTitle"]`).type('کتاب');
      cy.get(`[data-cy="levelTitle"]`).should('have.value', 'کتاب');

      cy.get(`[data-cy="type"]`).select('Credit');

      cy.get(`[data-cy="levelInTree"]`).select('KOL');

      cy.get(`[data-cy="debitAmount"]`).type('12189.3');
      cy.get(`[data-cy="debitAmount"]`).should('have.value', '12189.3');

      cy.get(`[data-cy="creditAmount"]`).type('26812.07');
      cy.get(`[data-cy="creditAmount"]`).should('have.value', '26812.07');

      cy.get(`[data-cy="typeInFormula"]`).select('ASSETS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accountHesab = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accountHesabPageUrlPattern);
    });
  });
});
