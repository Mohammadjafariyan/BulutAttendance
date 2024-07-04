import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { IAccProccParameter } from 'app/entities/BulutAttendance/acc-procc-parameter/acc-procc-parameter.model';
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';

export interface IAccProcStep {
  id: number;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  creditAccount?: IAccountHesab | null;
  debitAccount?: IAccountHesab | null;
  parameter?: IAccProccParameter | null;
  procedure?: IAccountingProcedure | null;
}

export type NewAccProcStep = Omit<IAccProcStep, 'id'> & { id: null };
