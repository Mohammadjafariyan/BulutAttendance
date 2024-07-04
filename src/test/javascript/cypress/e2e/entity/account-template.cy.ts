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

describe('AccountTemplate e2e test', () => {
  const accountTemplatePageUrl = '/bulutattendance/account-template';
  const accountTemplatePageUrlPattern = new RegExp('/bulutattendance/account-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accountTemplateSample = {};

  let accountTemplate;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/account-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/account-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/account-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/account-templates/${accountTemplate.id}`,
      }).then(() => {
        accountTemplate = undefined;
      });
    }
  });

  it('AccountTemplates menu should load AccountTemplates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/account-template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountTemplate').should('exist');
    cy.url().should('match', accountTemplatePageUrlPattern);
  });

  describe('AccountTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accountTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccountTemplate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/account-template/new$'));
        cy.getEntityCreateUpdateHeading('AccountTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/account-templates',
          body: accountTemplateSample,
        }).then(({ body }) => {
          accountTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/account-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/account-templates?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/account-templates?page=0&size=20>; rel="first"',
              },
              body: [accountTemplate],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accountTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccountTemplate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountTemplate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTemplatePageUrlPattern);
      });

      it('edit button click should load edit AccountTemplate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTemplatePageUrlPattern);
      });

      it('edit button click should load edit AccountTemplate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccountTemplate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of AccountTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('accountTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accountTemplatePageUrlPattern);

        accountTemplate = undefined;
      });
    });
  });

  describe('new AccountTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${accountTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccountTemplate');
    });

    it('should create an instance of AccountTemplate', () => {
      cy.get(`[data-cy="title"]`).type('ورزشی روزنامه');
      cy.get(`[data-cy="title"]`).should('have.value', 'ورزشی روزنامه');

      cy.get(`[data-cy="code"]`).type('پزشک anxiously usefully');
      cy.get(`[data-cy="code"]`).should('have.value', 'پزشک anxiously usefully');

      cy.get(`[data-cy="level"]`).type('4582');
      cy.get(`[data-cy="level"]`).should('have.value', '4582');

      cy.get(`[data-cy="levelTitle"]`).type('gratefully دستکش');
      cy.get(`[data-cy="levelTitle"]`).should('have.value', 'gratefully دستکش');

      cy.get(`[data-cy="type"]`).select('Debit');

      cy.get(`[data-cy="levelInTree"]`).select('TAFZIL_3');

      cy.get(`[data-cy="debitAmount"]`).type('26551.01');
      cy.get(`[data-cy="debitAmount"]`).should('have.value', '26551.01');

      cy.get(`[data-cy="creditAmount"]`).type('7213.61');
      cy.get(`[data-cy="creditAmount"]`).should('have.value', '7213.61');

      cy.get(`[data-cy="typeInFormula"]`).select('ASSETS');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accountTemplate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accountTemplatePageUrlPattern);
    });
  });
});
