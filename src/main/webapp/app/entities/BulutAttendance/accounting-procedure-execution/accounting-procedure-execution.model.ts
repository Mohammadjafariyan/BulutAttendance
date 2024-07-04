import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IAccountingProcedure } from 'app/entities/BulutAttendance/accounting-procedure/accounting-procedure.model';

export interface IAccountingProcedureExecution {
  id: number;
  dateTime?: dayjs.Dayjs | null;
  desc?: string | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  procedure?: IAccountingProcedure | null;
}

export type NewAccountingProcedureExecution = Omit<IAccountingProcedureExecution, 'id'> & { id: null };
