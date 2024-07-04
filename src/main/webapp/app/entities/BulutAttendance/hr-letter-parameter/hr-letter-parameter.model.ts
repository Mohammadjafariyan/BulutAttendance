import { IRecordStatus } from 'app/entities/BulutAttendance/record-status/record-status.model';
import { IApplicationUser } from 'app/entities/BulutAttendance/application-user/application-user.model';
import { ICompany } from 'app/entities/BulutAttendance/company/company.model';
import { CalcType } from 'app/entities/enumerations/calc-type.model';
import { CalcUnit } from 'app/entities/enumerations/calc-unit.model';
import { LaborTime } from 'app/entities/enumerations/labor-time.model';
import { Hokm } from 'app/entities/enumerations/hokm.model';
import { Earning } from 'app/entities/enumerations/earning.model';
import { Deduction } from 'app/entities/enumerations/deduction.model';

export interface IHrLetterParameter {
  id: number;
  title?: string | null;
  manualOrAuto?: keyof typeof CalcType | null;
  formula?: string | null;
  unit?: keyof typeof CalcUnit | null;
  isDeducTax?: boolean | null;
  isDeducInsurance?: boolean | null;
  laborTime?: keyof typeof LaborTime | null;
  hokm?: keyof typeof Hokm | null;
  earnings?: keyof typeof Earning | null;
  deduction?: keyof typeof Deduction | null;
  other?: keyof typeof Deduction | null;
  isEnabled?: boolean | null;
  status?: IRecordStatus | null;
  internalUser?: IApplicationUser | null;
  company?: ICompany | null;
}

export type NewHrLetterParameter = Omit<IHrLetterParameter, 'id'> & { id: null };
