import dayjs from 'dayjs/esm';
import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IHrLetterType } from 'app/entities/BulutAttendance/hr-letter-type/hr-letter-type.model';
import { IPersonnel } from 'app/entities/BulutAttendance/personnel/personnel.model';
import { IOrgPosition } from 'app/entities/BulutAttendance/org-position/org-position.model';
import { IOrgUnit } from 'app/entities/BulutAttendance/org-unit/org-unit.model';
import { IPersonnelStatus } from 'app/entities/BulutAttendance/personnel-status/personnel-status.model';
import { IHrLetterParameter } from 'app/entities/BulutAttendance/hr-letter-parameter/hr-letter-parameter.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';

export interface IHrLetter {
  title?: string | null;
  uniqueNumber?: string | null;
  id: string;
  issueDate?: dayjs.Dayjs | null;
  executionDate?: dayjs.Dayjs | null;
  bpmsApproveStatus?: number | null;
  status?: IRecordStatus | null;
  type?: IHrLetterType | null;
  personnelId?: IPersonnel | null;
  orgPosition?: IOrgPosition | null;
  orgUnit?: IOrgUnit | null;
  personnelStatus?: IPersonnelStatus | null;
  hrLetterParameter?: IHrLetterParameter | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewHrLetter = Omit<IHrLetter, 'id'> & { id: null };
