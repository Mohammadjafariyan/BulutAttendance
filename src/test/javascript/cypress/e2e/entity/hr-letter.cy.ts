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

describe('HrLetter e2e test', () => {
  const hrLetterPageUrl = '/bulutattendance/hr-letter';
  const hrLetterPageUrlPattern = new RegExp('/bulutattendance/hr-letter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hrLetterSample = {};

  let hrLetter;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/hr-letters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/hr-letters').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/hr-letters/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hrLetter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/hr-letters/${hrLetter.id}`,
      }).then(() => {
        hrLetter = undefined;
      });
    }
  });

  it('HrLetters menu should load HrLetters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/hr-letter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HrLetter').should('exist');
    cy.url().should('match', hrLetterPageUrlPattern);
  });

  describe('HrLetter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hrLetterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HrLetter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/hr-letter/new$'));
        cy.getEntityCreateUpdateHeading('HrLetter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/hr-letters',
          body: hrLetterSample,
        }).then(({ body }) => {
          hrLetter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/hr-letters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/hr-letters?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/hr-letters?page=0&size=20>; rel="first"',
              },
              body: [hrLetter],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hrLetterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HrLetter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hrLetter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterPageUrlPattern);
      });

      it('edit button click should load edit HrLetter page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterPageUrlPattern);
      });

      it('edit button click should load edit HrLetter page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetter');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterPageUrlPattern);
      });

      it('last delete button click should delete instance of HrLetter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('hrLetter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterPageUrlPattern);

        hrLetter = undefined;
      });
    });
  });

  describe('new HrLetter page', () => {
    beforeEach(() => {
      cy.visit(`${hrLetterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HrLetter');
    });

    it('should create an instance of HrLetter', () => {
      cy.get(`[data-cy="title"]`).type('گریه');
      cy.get(`[data-cy="title"]`).should('have.value', 'گریه');

      cy.get(`[data-cy="uniqueNumber"]`).type('روزنامه');
      cy.get(`[data-cy="uniqueNumber"]`).should('have.value', 'روزنامه');

      cy.get(`[data-cy="issueDate"]`).type('2024-06-10T08:23');
      cy.get(`[data-cy="issueDate"]`).blur();
      cy.get(`[data-cy="issueDate"]`).should('have.value', '2024-06-10T08:23');

      cy.get(`[data-cy="executionDate"]`).type('2024-06-09T17:29');
      cy.get(`[data-cy="executionDate"]`).blur();
      cy.get(`[data-cy="executionDate"]`).should('have.value', '2024-06-09T17:29');

      cy.get(`[data-cy="bpmsApproveStatus"]`).type('30788');
      cy.get(`[data-cy="bpmsApproveStatus"]`).should('have.value', '30788');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        hrLetter = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', hrLetterPageUrlPattern);
    });
  });
});
