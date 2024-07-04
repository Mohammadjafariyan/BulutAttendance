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

describe('OrgUnit e2e test', () => {
  const orgUnitPageUrl = '/bulutattendance/org-unit';
  const orgUnitPageUrlPattern = new RegExp('/bulutattendance/org-unit(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const orgUnitSample = {};

  let orgUnit;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/org-units+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/org-units').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/org-units/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (orgUnit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/org-units/${orgUnit.id}`,
      }).then(() => {
        orgUnit = undefined;
      });
    }
  });

  it('OrgUnits menu should load OrgUnits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/org-unit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrgUnit').should('exist');
    cy.url().should('match', orgUnitPageUrlPattern);
  });

  describe('OrgUnit page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orgUnitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrgUnit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/org-unit/new$'));
        cy.getEntityCreateUpdateHeading('OrgUnit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgUnitPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/org-units',
          body: orgUnitSample,
        }).then(({ body }) => {
          orgUnit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/org-units+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/org-units?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/org-units?page=0&size=20>; rel="first"',
              },
              body: [orgUnit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orgUnitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrgUnit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orgUnit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgUnitPageUrlPattern);
      });

      it('edit button click should load edit OrgUnit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrgUnit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgUnitPageUrlPattern);
      });

      it('edit button click should load edit OrgUnit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrgUnit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgUnitPageUrlPattern);
      });

      it('last delete button click should delete instance of OrgUnit', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orgUnit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', orgUnitPageUrlPattern);

        orgUnit = undefined;
      });
    });
  });

  describe('new OrgUnit page', () => {
    beforeEach(() => {
      cy.visit(`${orgUnitPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrgUnit');
    });

    it('should create an instance of OrgUnit', () => {
      cy.get(`[data-cy="title"]`).type('selfishly میمون');
      cy.get(`[data-cy="title"]`).should('have.value', 'selfishly میمون');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orgUnit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', orgUnitPageUrlPattern);
    });
  });
});
