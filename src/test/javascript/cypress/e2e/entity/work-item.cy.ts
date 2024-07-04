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

describe('WorkItem e2e test', () => {
  const workItemPageUrl = '/bulutattendance/work-item';
  const workItemPageUrlPattern = new RegExp('/bulutattendance/work-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const workItemSample = {};

  let workItem;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/work-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/work-items').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/work-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (workItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/work-items/${workItem.id}`,
      }).then(() => {
        workItem = undefined;
      });
    }
  });

  it('WorkItems menu should load WorkItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/work-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkItem').should('exist');
    cy.url().should('match', workItemPageUrlPattern);
  });

  describe('WorkItem page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WorkItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/work-item/new$'));
        cy.getEntityCreateUpdateHeading('WorkItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workItemPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/work-items',
          body: workItemSample,
        }).then(({ body }) => {
          workItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/work-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/work-items?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/work-items?page=0&size=20>; rel="first"',
              },
              body: [workItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(workItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WorkItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workItemPageUrlPattern);
      });

      it('edit button click should load edit WorkItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorkItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workItemPageUrlPattern);
      });

      it('edit button click should load edit WorkItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorkItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workItemPageUrlPattern);
      });

      it('last delete button click should delete instance of WorkItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('workItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workItemPageUrlPattern);

        workItem = undefined;
      });
    });
  });

  describe('new WorkItem page', () => {
    beforeEach(() => {
      cy.visit(`${workItemPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WorkItem');
    });

    it('should create an instance of WorkItem', () => {
      cy.get(`[data-cy="amount"]`).type('21356.38');
      cy.get(`[data-cy="amount"]`).should('have.value', '21356.38');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        workItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', workItemPageUrlPattern);
    });
  });
});
