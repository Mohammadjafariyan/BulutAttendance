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

describe('Personnel e2e test', () => {
  const personnelPageUrl = '/bulutattendance/personnel';
  const personnelPageUrlPattern = new RegExp('/bulutattendance/personnel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personnelSample = {};

  let personnel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/bulutattendance/api/personnel+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/bulutattendance/api/personnel').as('postEntityRequest');
    cy.intercept('DELETE', '/services/bulutattendance/api/personnel/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (personnel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/bulutattendance/api/personnel/${personnel.id}`,
      }).then(() => {
        personnel = undefined;
      });
    }
  });

  it('Personnel menu should load Personnel page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bulutattendance/personnel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Personnel').should('exist');
    cy.url().should('match', personnelPageUrlPattern);
  });

  describe('Personnel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personnelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Personnel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bulutattendance/personnel/new$'));
        cy.getEntityCreateUpdateHeading('Personnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/bulutattendance/api/personnel',
          body: personnelSample,
        }).then(({ body }) => {
          personnel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/bulutattendance/api/personnel+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/bulutattendance/api/personnel?page=0&size=20>; rel="last",<http://localhost/services/bulutattendance/api/personnel?page=0&size=20>; rel="first"',
              },
              body: [personnel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personnelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Personnel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('personnel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelPageUrlPattern);
      });

      it('edit button click should load edit Personnel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Personnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelPageUrlPattern);
      });

      it('edit button click should load edit Personnel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Personnel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelPageUrlPattern);
      });

      it('last delete button click should delete instance of Personnel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('personnel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personnelPageUrlPattern);

        personnel = undefined;
      });
    });
  });

  describe('new Personnel page', () => {
    beforeEach(() => {
      cy.visit(`${personnelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Personnel');
    });

    it('should create an instance of Personnel', () => {
      cy.get(`[data-cy="firstName"]`).type('بهنام');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'بهنام');

      cy.get(`[data-cy="lastName"]`).type('غنی');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'غنی');

      cy.get(`[data-cy="requitmentDate"]`).type('روشن');
      cy.get(`[data-cy="requitmentDate"]`).should('have.value', 'روشن');

      cy.get(`[data-cy="father"]`).type('perfectly');
      cy.get(`[data-cy="father"]`).should('have.value', 'perfectly');

      cy.get(`[data-cy="shenasname"]`).type('scarily');
      cy.get(`[data-cy="shenasname"]`).should('have.value', 'scarily');

      cy.get(`[data-cy="mahalesodur"]`).type('شهر');
      cy.get(`[data-cy="mahalesodur"]`).should('have.value', 'شهر');

      cy.get(`[data-cy="birthday"]`).type('بنابراین چون');
      cy.get(`[data-cy="birthday"]`).should('have.value', 'بنابراین چون');

      cy.get(`[data-cy="isSingle"]`).type('پشتیبانی کردن انتخاب کردن');
      cy.get(`[data-cy="isSingle"]`).should('have.value', 'پشتیبانی کردن انتخاب کردن');

      cy.get(`[data-cy="lastEducation"]`).type('بنابراین sheepishly');
      cy.get(`[data-cy="lastEducation"]`).should('have.value', 'بنابراین sheepishly');

      cy.get(`[data-cy="educationField"]`).type('در جلوی بلند به صورت');
      cy.get(`[data-cy="educationField"]`).should('have.value', 'در جلوی بلند به صورت');

      cy.get(`[data-cy="children"]`).type('42');
      cy.get(`[data-cy="children"]`).should('have.value', '42');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        personnel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', personnelPageUrlPattern);
    });
  });
});
