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

describe('HrLetterType e2e test', () => {
  const hrLetterTypePageUrl = '/bulutattendance/hr-letter-type';
  const hrLetterTypePageUrlPattern = new RegExp('/bulutattendance/hr-letter-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hrLetterTypeSample = {};

  let hrLetterType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/hr-letter-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/hr-letter-types').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/hr-letter-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hrLetterType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/hr-letter-types/${hrLetterType.id}`,
      }).then(() => {
        hrLetterType = undefined;
      });
    }
  });

  it('HrLetterTypes menu should load HrLetterTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/hr-letter-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HrLetterType').should('exist');
    cy.url().should('match', hrLetterTypePageUrlPattern);
  });

  describe('HrLetterType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hrLetterTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HrLetterType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/hr-letter-type/new$'));
        cy.getEntityCreateUpdateHeading('HrLetterType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/hr-letter-types',
          body: hrLetterTypeSample,
        }).then(({ body }) => {
          hrLetterType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/hr-letter-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/hr-letter-types?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/hr-letter-types?page=0&size=20>; rel="first"',
              },
              body: [hrLetterType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hrLetterTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HrLetterType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hrLetterType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterTypePageUrlPattern);
      });

      it('edit button click should load edit HrLetterType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetterType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterTypePageUrlPattern);
      });

      it('edit button click should load edit HrLetterType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HrLetterType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterTypePageUrlPattern);
      });

      it('last delete button click should delete instance of HrLetterType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('hrLetterType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hrLetterTypePageUrlPattern);

        hrLetterType = undefined;
      });
    });
  });

  describe('new HrLetterType page', () => {
    beforeEach(() => {
      cy.visit(`${hrLetterTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HrLetterType');
    });

    it('should create an instance of HrLetterType', () => {
      cy.get(`[data-cy="title"]`).type('در مقابل چون هر چند');
      cy.get(`[data-cy="title"]`).should('have.value', 'در مقابل چون هر چند');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        hrLetterType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', hrLetterTypePageUrlPattern);
    });
  });
});
