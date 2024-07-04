import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IAccountHesab } from 'app/entities/BulutAttendance/account-hesab/account-hesab.model';
import { ITransaction } from 'app/entities/BulutAttendance/transaction/transaction.model';

export interface ITransactionAccount {
  id: number;
  debitAmount?: number | null;
  creditAmount?: number | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  account?: IAccountHesab | null;
  transaction?: ITransaction | null;
}

export type NewTransactionAccount = Omit<ITransactionAccount, 'id'> & { id: null };
