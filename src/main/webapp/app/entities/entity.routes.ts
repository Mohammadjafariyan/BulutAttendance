import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'account-hesab',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccountHesab.home.title' },
    loadChildren: () => import('./BulutAttendance/account-hesab/account-hesab.routes'),
  },
  {
    path: 'accounting-procedure',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccountingProcedure.home.title' },
    loadChildren: () => import('./BulutAttendance/accounting-procedure/accounting-procedure.routes'),
  },
  {
    path: 'accounting-procedure-execution',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccountingProcedureExecution.home.title' },
    loadChildren: () => import('./BulutAttendance/accounting-procedure-execution/accounting-procedure-execution.routes'),
  },
  {
    path: 'account-template',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccountTemplate.home.title' },
    loadChildren: () => import('./BulutAttendance/account-template/account-template.routes'),
  },
  {
    path: 'acc-procc-parameter',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccProccParameter.home.title' },
    loadChildren: () => import('./BulutAttendance/acc-procc-parameter/acc-procc-parameter.routes'),
  },
  {
    path: 'acc-proc-step',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccProcStep.home.title' },
    loadChildren: () => import('./BulutAttendance/acc-proc-step/acc-proc-step.routes'),
  },
  {
    path: 'acc-proc-step-execution',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceAccProcStepExecution.home.title' },
    loadChildren: () => import('./BulutAttendance/acc-proc-step-execution/acc-proc-step-execution.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceApplicationUser.home.title' },
    loadChildren: () => import('./BulutAttendance/application-user/application-user.routes'),
  },
  {
    path: 'bank',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceBank.home.title' },
    loadChildren: () => import('./BulutAttendance/bank/bank.routes'),
  },
  {
    path: 'company',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceCompany.home.title' },
    loadChildren: () => import('./BulutAttendance/company/company.routes'),
  },
  {
    path: 'hr-letter',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceHrLetter.home.title' },
    loadChildren: () => import('./BulutAttendance/hr-letter/hr-letter.routes'),
  },
  {
    path: 'hr-letter-parameter',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceHrLetterParameter.home.title' },
    loadChildren: () => import('./BulutAttendance/hr-letter-parameter/hr-letter-parameter.routes'),
  },
  {
    path: 'hr-letter-type',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceHrLetterType.home.title' },
    loadChildren: () => import('./BulutAttendance/hr-letter-type/hr-letter-type.routes'),
  },
  {
    path: 'leave',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceLeave.home.title' },
    loadChildren: () => import('./BulutAttendance/leave/leave.routes'),
  },
  {
    path: 'leave-summary',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceLeaveSummary.home.title' },
    loadChildren: () => import('./BulutAttendance/leave-summary/leave-summary.routes'),
  },
  {
    path: 'org-position',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceOrgPosition.home.title' },
    loadChildren: () => import('./BulutAttendance/org-position/org-position.routes'),
  },
  {
    path: 'org-unit',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceOrgUnit.home.title' },
    loadChildren: () => import('./BulutAttendance/org-unit/org-unit.routes'),
  },
  {
    path: 'parameter',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceParameter.home.title' },
    loadChildren: () => import('./BulutAttendance/parameter/parameter.routes'),
  },
  {
    path: 'personnel',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendancePersonnel.home.title' },
    loadChildren: () => import('./BulutAttendance/personnel/personnel.routes'),
  },
  {
    path: 'personnel-status',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendancePersonnelStatus.home.title' },
    loadChildren: () => import('./BulutAttendance/personnel-status/personnel-status.routes'),
  },
  {
    path: 'record-status',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceRecordStatus.home.title' },
    loadChildren: () => import('./BulutAttendance/record-status/record-status.routes'),
  },
  {
    path: 'sys-config',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceSysConfig.home.title' },
    loadChildren: () => import('./BulutAttendance/sys-config/sys-config.routes'),
  },
  {
    path: 'tax-template',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceTaxTemplate.home.title' },
    loadChildren: () => import('./BulutAttendance/tax-template/tax-template.routes'),
  },
  {
    path: 'transaction',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceTransaction.home.title' },
    loadChildren: () => import('./BulutAttendance/transaction/transaction.routes'),
  },
  {
    path: 'transaction-account',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceTransactionAccount.home.title' },
    loadChildren: () => import('./BulutAttendance/transaction-account/transaction-account.routes'),
  },
  {
    path: 'work',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceWork.home.title' },
    loadChildren: () => import('./BulutAttendance/work/work.routes'),
  },
  {
    path: 'work-item',
    data: { pageTitle: 'bulutAttendanceApp.bulutAttendanceWorkItem.home.title' },
    loadChildren: () => import('./BulutAttendance/work-item/work-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
