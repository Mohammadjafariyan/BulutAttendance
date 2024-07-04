import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { IWork } from 'app/entities/BulutAttendance/work/work.model';

export interface IWorkItem {
  id: string;
  amount?: number | null;
  hrLetterParameter?: IHrLetterParameter | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
  work?: IWork | null;
}

export type NewWorkItem = Omit<IWorkItem, 'id'> & { id: null };
