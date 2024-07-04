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

describe('PersonnelStatus e2e test', () => {
  const personnelStatusPageUrl = '/bulutattendance/personnel-status';
  const personnelStatusPageUrlPattern = new RegExp('/bulutattendance/personnel-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personnelStatusSample = {};

  let personnelStatus;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/personnel-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/personnel-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/personnel-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personnelStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/personnel-statuses/${personnelStatus.id}`,
      }).then(() => {
        personnelStatus = undefined;
      });
    }
  });

  it('PersonnelStatuses menu should load PersonnelStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/personnel-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PersonnelStatus').should('exist');
    cy.url().should('match', personnelStatusPageUrlPattern);
  });

  describe('PersonnelStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personnelStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PersonnelStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/personnel-status/new$'));
        cy.getEntityCreateUpdateHeading('PersonnelStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/personnel-statuses',
          body: personnelStatusSample,
        }).then(({ body }) => {
          personnelStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/personnel-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/personnel-statuses?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/personnel-statuses?page=0&size=20>; rel="first"',
              },
              body: [personnelStatus],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personnelStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PersonnelStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personnelStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelStatusPageUrlPattern);
      });

      it('edit button click should load edit PersonnelStatus page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonnelStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelStatusPageUrlPattern);
      });

      it('edit button click should load edit PersonnelStatus page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PersonnelStatus');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of PersonnelStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('personnelStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelStatusPageUrlPattern);

        personnelStatus = undefined;
      });
    });
  });

  describe('new PersonnelStatus page', () => {
    beforeEach(() => {
      cy.visit(`${personnelStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PersonnelStatus');
    });

    it('should create an instance of PersonnelStatus', () => {
      cy.get(`[data-cy="title"]`).type('بلکه واو دریا');
      cy.get(`[data-cy="title"]`).should('have.value', 'بلکه واو دریا');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        personnelStatus = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', personnelStatusPageUrlPattern);
    });
  });
});
