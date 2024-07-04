import dayjs from 'dayjs/esm';
import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface ILeave {
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  id: string;
  bpmsApproveStatus?: number | null;
  status?: IRecordStatus | null;
  personnelId?: IPersonnel | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewLeave = Omit<ILeave, 'id'> & { id: null };
