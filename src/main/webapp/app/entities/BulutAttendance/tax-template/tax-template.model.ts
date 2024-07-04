import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface ITaxTemplate {
  rangeFrom?: number | null;
  rangeTo?: number | null;
  percent?: number | null;
  year?: number | null;
  id: string;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewTaxTemplate = Omit<ITaxTemplate, 'id'> & { id: null };
