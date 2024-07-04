import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface ILeaveSummary {
  remainHours?: number | null;
  remainDays?: number | null;
  year?: number | null;
  id: string;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewLeaveSummary = Omit<ILeaveSummary, 'id'> & { id: null };
