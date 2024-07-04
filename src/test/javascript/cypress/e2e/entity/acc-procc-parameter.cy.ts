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

describe('AccProccParameter e2e test', () => {
  const accProccParameterPageUrl = '/bulutattendance/acc-procc-parameter';
  const accProccParameterPageUrlPattern = new RegExp('/bulutattendance/acc-procc-parameter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accProccParameterSample = {};

  let accProccParameter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/acc-procc-parameters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/acc-procc-parameters').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/acc-procc-parameters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accProccParameter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/acc-procc-parameters/${accProccParameter.id}`,
      }).then(() => {
        accProccParameter = undefined;
      });
    }
  });

  it('AccProccParameters menu should load AccProccParameters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/acc-procc-parameter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccProccParameter').should('exist');
    cy.url().should('match', accProccParameterPageUrlPattern);
  });

  describe('AccProccParameter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accProccParameterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccProccParameter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/acc-procc-parameter/new$'));
        cy.getEntityCreateUpdateHeading('AccProccParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProccParameterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/acc-procc-parameters',
          body: accProccParameterSample,
        }).then(({ body }) => {
          accProccParameter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/acc-procc-parameters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/acc-procc-parameters?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/acc-procc-parameters?page=0&size=20>; rel="first"',
              },
              body: [accProccParameter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accProccParameterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccProccParameter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accProccParameter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProccParameterPageUrlPattern);
      });

      it('edit button click should load edit AccProccParameter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProccParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProccParameterPageUrlPattern);
      });

      it('edit button click should load edit AccProccParameter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccProccParameter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProccParameterPageUrlPattern);
      });

      it('last delete button click should delete instance of AccProccParameter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accProccParameter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accProccParameterPageUrlPattern);

        accProccParameter = undefined;
      });
    });
  });

  describe('new AccProccParameter page', () => {
    beforeEach(() => {
      cy.visit(`${accProccParameterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccProccParameter');
    });

    it('should create an instance of AccProccParameter', () => {
      cy.get(`[data-cy="title"]`).type('یاالله');
      cy.get(`[data-cy="title"]`).should('have.value', 'یاالله');

      cy.get(`[data-cy="manualOrAuto"]`).select('BySystem');

      cy.get(`[data-cy="formula"]`).type('clearly دشوار دیدن');
      cy.get(`[data-cy="formula"]`).should('have.value', 'clearly دشوار دیدن');

      cy.get(`[data-cy="unit"]`).select('Daily');

      cy.get(`[data-cy="isDeducTax"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducTax"]`).click();
      cy.get(`[data-cy="isDeducTax"]`).should('be.checked');

      cy.get(`[data-cy="isDeducInsurance"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducInsurance"]`).click();
      cy.get(`[data-cy="isDeducInsurance"]`).should('be.checked');

      cy.get(`[data-cy="laborTime"]`).select('HOLIDAY_WORK');

      cy.get(`[data-cy="hokm"]`).select('LABOR');

      cy.get(`[data-cy="earnings"]`).select('BONUS');

      cy.get(`[data-cy="deduction"]`).select('Insurance_Labor');

      cy.get(`[data-cy="other"]`).select('Insurance_Labor');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accProccParameter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accProccParameterPageUrlPattern);
    });
  });
});
