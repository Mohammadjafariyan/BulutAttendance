import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface IAccountingProcedure {
  id: number;
  title?: string | null;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  executeAfter?: IAccountingProcedure | null;
}

export type NewAccountingProcedure = Omit<IAccountingProcedure, 'id'> & { id: null };
