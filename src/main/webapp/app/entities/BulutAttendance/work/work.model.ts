import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface IWork {
  id: string;
  issueDate?: dayjs.Dayjs | null;
  desc?: string | null;
  year?: number | null;
  month?: number | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewWork = Omit<IWork, 'id'> & { id: null };
