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

describe('RecordStatus e2e test', () => {
  const recordStatusPageUrl = '/bulutattendance/record-status';
  const recordStatusPageUrlPattern = new RegExp('/bulutattendance/record-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const recordStatusSample = {};

  let recordStatus;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/record-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/record-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/record-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recordStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/record-statuses/${recordStatus.id}`,
      }).then(() => {
        recordStatus = undefined;
      });
    }
  });

  it('RecordStatuses menu should load RecordStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/record-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RecordStatus').should('exist');
    cy.url().should('match', recordStatusPageUrlPattern);
  });

  describe('RecordStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recordStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RecordStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/record-status/new$'));
        cy.getEntityCreateUpdateHeading('RecordStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', recordStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/record-statuses',
          body: recordStatusSample,
        }).then(({ body }) => {
          recordStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/record-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [recordStatus],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(recordStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RecordStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recordStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', recordStatusPageUrlPattern);
      });

      it('edit button click should load edit RecordStatus page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecordStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', recordStatusPageUrlPattern);
      });

      it('edit button click should load edit RecordStatus page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecordStatus');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', recordStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of RecordStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('recordStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', recordStatusPageUrlPattern);

        recordStatus = undefined;
      });
    });
  });

  describe('new RecordStatus page', () => {
    beforeEach(() => {
      cy.visit(`${recordStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RecordStatus');
    });

    it('should create an instance of RecordStatus', () => {
      cy.get(`[data-cy="fromDateTime"]`).type('2024-06-09T17:03');
      cy.get(`[data-cy="fromDateTime"]`).blur();
      cy.get(`[data-cy="fromDateTime"]`).should('have.value', '2024-06-09T17:03');

      cy.get(`[data-cy="toDateTime"]`).type('2024-06-10T07:24');
      cy.get(`[data-cy="toDateTime"]`).blur();
      cy.get(`[data-cy="toDateTime"]`).should('have.value', '2024-06-10T07:24');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        recordStatus = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', recordStatusPageUrlPattern);
    });
  });
});
