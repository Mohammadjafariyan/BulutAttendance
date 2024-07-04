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

describe('Work e2e test', () => {
  const workPageUrl = '/bulutattendance/work';
  const workPageUrlPattern = new RegExp('/bulutattendance/work(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const workSample = {};

  let work;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/works+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/works').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/works/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (work) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/works/${work.id}`,
      }).then(() => {
        work = undefined;
      });
    }
  });

  it('Works menu should load Works page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/work');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Work').should('exist');
    cy.url().should('match', workPageUrlPattern);
  });

  describe('Work page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Work page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/work/new$'));
        cy.getEntityCreateUpdateHeading('Work');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/works',
          body: workSample,
        }).then(({ body }) => {
          work = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/works+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/works?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/works?page=0&size=20>; rel="first"',
              },
              body: [work],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(workPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Work page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('work');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workPageUrlPattern);
      });

      it('edit button click should load edit Work page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Work');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workPageUrlPattern);
      });

      it('edit button click should load edit Work page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Work');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workPageUrlPattern);
      });

      it('last delete button click should delete instance of Work', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('work').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', workPageUrlPattern);

        work = undefined;
      });
    });
  });

  describe('new Work page', () => {
    beforeEach(() => {
      cy.visit(`${workPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Work');
    });

    it('should create an instance of Work', () => {
      cy.get(`[data-cy="issueDate"]`).type('2024-06-09T18:43');
      cy.get(`[data-cy="issueDate"]`).blur();
      cy.get(`[data-cy="issueDate"]`).should('have.value', '2024-06-09T18:43');

      cy.get(`[data-cy="desc"]`).type('قبول کردن');
      cy.get(`[data-cy="desc"]`).should('have.value', 'قبول کردن');

      cy.get(`[data-cy="year"]`).type('665');
      cy.get(`[data-cy="year"]`).should('have.value', '665');

      cy.get(`[data-cy="month"]`).type('16401');
      cy.get(`[data-cy="month"]`).should('have.value', '16401');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        work = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', workPageUrlPattern);
    });
  });
});
