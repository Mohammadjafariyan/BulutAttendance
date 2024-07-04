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

describe('HrLetterParameter e2e test', () => {
  const hrLetterParameterPageUrl = '/bulutattendance/hr-letter-parameter';
  const hrLetterParameterPageUrlPattern = new RegExp('/bulutattendance/hr-letter-parameter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hrLetterParameterSample = {};

  let hrLetterParameter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/hr-letter-parameters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/hr-letter-parameters').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/hr-letter-parameters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hrLetterParameter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/hr-letter-parameters/${hrLetterParameter.id}`,
      }).then(() => {
        hrLetterParameter = undefined;
      });
    }
  });

  it('HrLetterParameters menu should load HrLetterParameters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/hr-letter-parameter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HrLetterParameter').should('exist');
    cy.url().should('match', hrLetterParameterPageUrlPattern);
  });

  describe('HrLetterParameter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hrLetterParameterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HrLetterParameter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/hr-letter-parameter/new$'));
        cy.getEntityCreateUpdateHeading('HrLetterParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterParameterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/hr-letter-parameters',
          body: hrLetterParameterSample,
        }).then(({ body }) => {
          hrLetterParameter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/hr-letter-parameters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [hrLetterParameter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hrLetterParameterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HrLetterParameter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hrLetterParameter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterParameterPageUrlPattern);
      });

      it('edit button click should load edit HrLetterParameter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetterParameter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterParameterPageUrlPattern);
      });

      it('edit button click should load edit HrLetterParameter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetterParameter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterParameterPageUrlPattern);
      });

      it('last delete button click should delete instance of HrLetterParameter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('hrLetterParameter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterParameterPageUrlPattern);

        hrLetterParameter = undefined;
      });
    });
  });

  describe('new HrLetterParameter page', () => {
    beforeEach(() => {
      cy.visit(`${hrLetterParameterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HrLetterParameter');
    });

    it('should create an instance of HrLetterParameter', () => {
      cy.get(`[data-cy="title"]`).type('sleepily وای');
      cy.get(`[data-cy="title"]`).should('have.value', 'sleepily وای');

      cy.get(`[data-cy="manualOrAuto"]`).select('ByHand');

      cy.get(`[data-cy="formula"]`).type('فراموش کردن پایان دادن');
      cy.get(`[data-cy="formula"]`).should('have.value', 'فراموش کردن پایان دادن');

      cy.get(`[data-cy="unit"]`).select('Hourly');

      cy.get(`[data-cy="isDeducTax"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducTax"]`).click();
      cy.get(`[data-cy="isDeducTax"]`).should('be.checked');

      cy.get(`[data-cy="isDeducInsurance"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeducInsurance"]`).click();
      cy.get(`[data-cy="isDeducInsurance"]`).should('be.checked');

      cy.get(`[data-cy="laborTime"]`).select('TOTAL_WORK');

      cy.get(`[data-cy="hokm"]`).select('HOUSE');

      cy.get(`[data-cy="earnings"]`).select('Total');

      cy.get(`[data-cy="deduction"]`).select('Total_Deduction');

      cy.get(`[data-cy="other"]`).select('Total_Deduction');

      cy.get(`[data-cy="isEnabled"]`).should('not.be.checked');
      cy.get(`[data-cy="isEnabled"]`).click();
      cy.get(`[data-cy="isEnabled"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        hrLetterParameter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', hrLetterParameterPageUrlPattern);
    });
  });
});
