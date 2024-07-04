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

describe('LeaveSummary e2e test', () => {
  const leaveSummaryPageUrl = '/bulutattendance/leave-summary';
  const leaveSummaryPageUrlPattern = new RegExp('/bulutattendance/leave-summary(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const leaveSummarySample = {};

  let leaveSummary;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/leave-summaries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/leave-summaries').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/leave-summaries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaveSummary) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/leave-summaries/${leaveSummary.id}`,
      }).then(() => {
        leaveSummary = undefined;
      });
    }
  });

  it('LeaveSummaries menu should load LeaveSummaries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/leave-summary');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaveSummary').should('exist');
    cy.url().should('match', leaveSummaryPageUrlPattern);
  });

  describe('LeaveSummary page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaveSummaryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaveSummary page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/leave-summary/new$'));
        cy.getEntityCreateUpdateHeading('LeaveSummary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', leaveSummaryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/leave-summaries',
          body: leaveSummarySample,
        }).then(({ body }) => {
          leaveSummary = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/leave-summaries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/leave-summaries?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/leave-summaries?page=0&size=20>; rel="first"',
              },
              body: [leaveSummary],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaveSummaryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LeaveSummary page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaveSummary');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', leaveSummaryPageUrlPattern);
      });

      it('edit button click should load edit LeaveSummary page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaveSummary');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', leaveSummaryPageUrlPattern);
      });

      it('edit button click should load edit LeaveSummary page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaveSummary');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', leaveSummaryPageUrlPattern);
      });

      it('last delete button click should delete instance of LeaveSummary', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaveSummary').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', leaveSummaryPageUrlPattern);

        leaveSummary = undefined;
      });
    });
  });

  describe('new LeaveSummary page', () => {
    beforeEach(() => {
      cy.visit(`${leaveSummaryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LeaveSummary');
    });

    it('should create an instance of LeaveSummary', () => {
      cy.get(`[data-cy="remainHours"]`).type('30807');
      cy.get(`[data-cy="remainHours"]`).should('have.value', '30807');

      cy.get(`[data-cy="remainDays"]`).type('28621');
      cy.get(`[data-cy="remainDays"]`).should('have.value', '28621');

      cy.get(`[data-cy="year"]`).type('6258');
      cy.get(`[data-cy="year"]`).should('have.value', '6258');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        leaveSummary = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', leaveSummaryPageUrlPattern);
    });
  });
});
