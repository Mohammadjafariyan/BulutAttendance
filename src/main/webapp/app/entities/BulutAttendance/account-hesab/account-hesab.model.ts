import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { IBank } from 'app/entities/BulutAttendance/bank/bank.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountLevelInTree } from 'app/entities/enumerations/account-level-in-tree.model';
import { AccountingFormulaType } from 'app/entities/enumerations/accounting-formula-type.model';

export interface IAccountHesab {
  title?: string | null;
  code?: string | null;
  level?: number | null;
  levelTitle?: string | null;
  id: string;
  type?: keyof typeof AccountType | null;
  levelInTree?: keyof typeof AccountLevelInTree | null;
  debitAmount?: number | null;
  creditAmount?: number | null;
  typeInFormula?: keyof typeof AccountingFormulaType | null;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  parentAccountId?: IAccountHesab | null;
  personnelId?: IPersonnel | null;
  bank?: IBank | null;
}

export type NewAccountHesab = Omit<IAccountHesab, 'id'> & { id: null };
