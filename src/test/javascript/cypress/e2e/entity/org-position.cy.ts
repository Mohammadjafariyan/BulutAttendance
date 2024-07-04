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

describe('OrgPosition e2e test', () => {
  const orgPositionPageUrl = '/bulutattendance/org-position';
  const orgPositionPageUrlPattern = new RegExp('/bulutattendance/org-position(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const orgPositionSample = {};

  let orgPosition;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/org-positions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/org-positions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/org-positions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orgPosition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/org-positions/${orgPosition.id}`,
      }).then(() => {
        orgPosition = undefined;
      });
    }
  });

  it('OrgPositions menu should load OrgPositions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/org-position');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrgPosition').should('exist');
    cy.url().should('match', orgPositionPageUrlPattern);
  });

  describe('OrgPosition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orgPositionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrgPosition page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/org-position/new$'));
        cy.getEntityCreateUpdateHeading('OrgPosition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgPositionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/org-positions',
          body: orgPositionSample,
        }).then(({ body }) => {
          orgPosition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/org-positions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/org-positions?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/org-positions?page=0&size=20>; rel="first"',
              },
              body: [orgPosition],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orgPositionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrgPosition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orgPosition');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgPositionPageUrlPattern);
      });

      it('edit button click should load edit OrgPosition page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrgPosition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgPositionPageUrlPattern);
      });

      it('edit button click should load edit OrgPosition page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrgPosition');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgPositionPageUrlPattern);
      });

      it('last delete button click should delete instance of OrgPosition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orgPosition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgPositionPageUrlPattern);

        orgPosition = undefined;
      });
    });
  });

  describe('new OrgPosition page', () => {
    beforeEach(() => {
      cy.visit(`${orgPositionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrgPosition');
    });

    it('should create an instance of OrgPosition', () => {
      cy.get(`[data-cy="title"]`).type('tremendously برای باور کردن');
      cy.get(`[data-cy="title"]`).should('have.value', 'tremendously برای باور کردن');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orgPosition = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', orgPositionPageUrlPattern);
    });
  });
});
