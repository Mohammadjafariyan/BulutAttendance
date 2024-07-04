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

describe('SysConfig e2e test', () => {
  const sysConfigPageUrl = '/bulutattendance/sys-config';
  const sysConfigPageUrlPattern = new RegExp('/bulutattendance/sys-config(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sysConfigSample = {};

  let sysConfig;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/sys-configs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/sys-configs').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/sys-configs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sysConfig) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/sys-configs/${sysConfig.id}`,
      }).then(() => {
        sysConfig = undefined;
      });
    }
  });

  it('SysConfigs menu should load SysConfigs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/sys-config');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SysConfig').should('exist');
    cy.url().should('match', sysConfigPageUrlPattern);
  });

  describe('SysConfig page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sysConfigPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SysConfig page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/sys-config/new$'));
        cy.getEntityCreateUpdateHeading('SysConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysConfigPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/sys-configs',
          body: sysConfigSample,
        }).then(({ body }) => {
          sysConfig = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/sys-configs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/sys-configs?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/sys-configs?page=0&size=20>; rel="first"',
              },
              body: [sysConfig],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sysConfigPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SysConfig page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sysConfig');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysConfigPageUrlPattern);
      });

      it('edit button click should load edit SysConfig page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysConfig');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysConfigPageUrlPattern);
      });

      it('edit button click should load edit SysConfig page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SysConfig');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysConfigPageUrlPattern);
      });

      it('last delete button click should delete instance of SysConfig', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sysConfig').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sysConfigPageUrlPattern);

        sysConfig = undefined;
      });
    });
  });

  describe('new SysConfig page', () => {
    beforeEach(() => {
      cy.visit(`${sysConfigPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SysConfig');
    });

    it('should create an instance of SysConfig', () => {
      cy.get(`[data-cy="taxFormula"]`).type('به هیچ وجه');
      cy.get(`[data-cy="taxFormula"]`).should('have.value', 'به هیچ وجه');

      cy.get(`[data-cy="sanavatFormula"]`).type('آه رفتن خسته‌کننده');
      cy.get(`[data-cy="sanavatFormula"]`).should('have.value', 'آه رفتن خسته‌کننده');

      cy.get(`[data-cy="year"]`).type('23461');
      cy.get(`[data-cy="year"]`).should('have.value', '23461');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        sysConfig = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', sysConfigPageUrlPattern);
    });
  });
});
