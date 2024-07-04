import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { IAccountingProcedureExecution } from 'app/entities/BulutAttendance/accounting-procedure-execution/accounting-procedure-execution.model';
import { IAccProcStep } from 'app/entities/BulutAttendance/acc-proc-step/acc-proc-step.model';

export interface IAccProcStepExecution {
  id: number;
  debitAmount?: number | null;
  creditAmount?: number | null;
  desc?: string | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  creditAccount?: IAccountHesab | null;
  debitAccount?: IAccountHesab | null;
  procedure?: IAccountingProcedureExecution | null;
  step?: IAccProcStep | null;
}

export type NewAccProcStepExecution = Omit<IAccProcStepExecution, 'id'> & { id: null };
