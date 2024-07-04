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

describe('Parameter e2e test', () => {
  const parameterPageUrl = '/bulutattendance/parameter';
  const parameterPageUrlPattern = new RegExp('/bulutattendance/parameter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const parameterSample = {};

  let parameter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/parameters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/parameters').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/parameters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (parameter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/parameters/${parameter.id}`,
      }).then(() => {
        parameter = undefined;
      });
    }
  });

  it('Parameters menu should load Parameters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/parameter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Parameter').should('exist');
    cy.url().should('match', parameterPageUrlPattern);
  });

  describe('Parameter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(parameterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Parameter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/parameter/new$'));
        cy.getEntityCreateUpdateHeading('Parameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parameterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/parameters',
          body: parameterSample,
        }).then(({ body }) => {
          parameter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/parameters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/parameters?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/parameters?page=0&size=20>; rel="first"',
              },
              body: [parameter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(parameterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Parameter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('parameter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parameterPageUrlPattern);
      });

      it('edit button click should load edit Parameter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Parameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parameterPageUrlPattern);
      });

      it('edit button click should load edit Parameter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Parameter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parameterPageUrlPattern);
      });

      it('last delete button click should delete instance of Parameter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('parameter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parameterPageUrlPattern);

        parameter = undefined;
      });
    });
  });

  describe('new Parameter page', () => {
    beforeEach(() => {
      cy.visit(`${parameterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Parameter');
    });

    it('should create an instance of Parameter', () => {
      cy.get(`[data-cy="title"]`).type('اوف');
      cy.get(`[data-cy="title"]`).should('have.value', 'اوف');

      cy.get(`[data-cy="manualOrAuto"]`).select('BySystem');

      cy.get(`[data-cy="formula"]`).type('bitterly پیروی کردن');
      cy.get(`[data-cy="formula"]`).should('have.value', 'bitterly پیروی کردن');

      cy.get(`[data-cy="unit"]`).select('Daily');

      cy.get(`[data-cy="isDeducTax"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducTax"]`).click();
      cy.get(`[data-cy="isDeducTax"]`).should('be.checked');

      cy.get(`[data-cy="isDeducInsurance"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducInsurance"]`).click();
      cy.get(`[data-cy="isDeducInsurance"]`).should('be.checked');

      cy.get(`[data-cy="laborTime"]`).select('SHIFT_22_5');

      cy.get(`[data-cy="hokm"]`).select('ROLE');

      cy.get(`[data-cy="earnings"]`).select('Tax_Insurance');

      cy.get(`[data-cy="deduction"]`).select('Loan');

      cy.get(`[data-cy="other"]`).select('Tax');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        parameter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', parameterPageUrlPattern);
    });
  });
});
