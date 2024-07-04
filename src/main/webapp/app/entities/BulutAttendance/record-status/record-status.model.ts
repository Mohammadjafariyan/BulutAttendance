import dayjs from 'dayjs/esm';

export interface IRecordStatus {
  fromDateTime?: dayjs.Dayjs | null;
  toDateTime?: dayjs.Dayjs | null;
  isDeleted?: boolean | null;
  id: string;
}

export type NewRecordStatus = Omit<IRecordStatus, 'id'> & { id: null };
