import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface ITransaction {
  id: number;
  issueDateTime?: dayjs.Dayjs | null;
  totalAmount?: number | null;
  desc?: string | null;
  docNumber?: string | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
