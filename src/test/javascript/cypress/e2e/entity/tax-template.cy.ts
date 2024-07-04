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

describe('TaxTemplate e2e test', () => {
  const taxTemplatePageUrl = '/bulutattendance/tax-template';
  const taxTemplatePageUrlPattern = new RegExp('/bulutattendance/tax-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const taxTemplateSample = {};

  let taxTemplate;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/tax-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/tax-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/tax-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (taxTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/tax-templates/${taxTemplate.id}`,
      }).then(() => {
        taxTemplate = undefined;
      });
    }
  });

  it('TaxTemplates menu should load TaxTemplates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/tax-template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TaxTemplate').should('exist');
    cy.url().should('match', taxTemplatePageUrlPattern);
  });

  describe('TaxTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(taxTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TaxTemplate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/tax-template/new$'));
        cy.getEntityCreateUpdateHeading('TaxTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/tax-templates',
          body: taxTemplateSample,
        }).then(({ body }) => {
          taxTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/tax-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/tax-templates?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/tax-templates?page=0&size=20>; rel="first"',
              },
              body: [taxTemplate],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(taxTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TaxTemplate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('taxTemplate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxTemplatePageUrlPattern);
      });

      it('edit button click should load edit TaxTemplate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxTemplatePageUrlPattern);
      });

      it('edit button click should load edit TaxTemplate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TaxTemplate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of TaxTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('taxTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', taxTemplatePageUrlPattern);

        taxTemplate = undefined;
      });
    });
  });

  describe('new TaxTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${taxTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TaxTemplate');
    });

    it('should create an instance of TaxTemplate', () => {
      cy.get(`[data-cy="rangeFrom"]`).type('24689.6');
      cy.get(`[data-cy="rangeFrom"]`).should('have.value', '24689.6');

      cy.get(`[data-cy="rangeTo"]`).type('11936.93');
      cy.get(`[data-cy="rangeTo"]`).should('have.value', '11936.93');

      cy.get(`[data-cy="percent"]`).type('10317');
      cy.get(`[data-cy="percent"]`).should('have.value', '10317');

      cy.get(`[data-cy="year"]`).type('21471');
      cy.get(`[data-cy="year"]`).should('have.value', '21471');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        taxTemplate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', taxTemplatePageUrlPattern);
    });
  });
});
