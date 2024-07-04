import { IPersonnelStatus, NewPersonnelStatus } from './personnel-status.model';

export const sampleWithRequiredData: IPersonnelStatus = {
  id: 'aeae43ea-e8ea-49c0-9a19-298edcc430e0',
};

export const sampleWithPartialData: IPersonnelStatus = {
  title: 'کارگر وایسا همچنین',
  id: '73797ac6-260d-4f21-85e5-df0dfb8f1a89',
};

export const sampleWithFullData: IPersonnelStatus = {
  title: 'هر چند',
  id: '8dbd19db-5fee-4018-bfdb-4cfbc0d6c154',
};

export const sampleWithNewData: NewPersonnelStatus = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
