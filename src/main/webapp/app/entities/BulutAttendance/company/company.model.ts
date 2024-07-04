import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';

export interface ICompany {
  id: number;
  title?: string | null;
  logo?: string | null;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
