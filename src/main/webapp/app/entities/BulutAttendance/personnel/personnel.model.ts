import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface IPersonnel {
  firstName?: string | null;
  lastName?: string | null;
  requitmentDate?: string | null;
  father?: string | null;
  shenasname?: string | null;
  mahalesodur?: string | null;
  birthday?: string | null;
  isSingle?: string | null;
  lastEducation?: string | null;
  educationField?: string | null;
  children?: number | null;
  id: string;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewPersonnel = Omit<IPersonnel, 'id'> & { id: null };
