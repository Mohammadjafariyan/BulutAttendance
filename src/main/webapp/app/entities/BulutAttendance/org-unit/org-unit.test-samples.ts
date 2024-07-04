import { IOrgUnit, NewOrgUnit } from './org-unit.model';

export const sampleWithRequiredData: IOrgUnit = {
  id: '0928adce-a110-4b40-ae6c-7a0649aa2570',
};

export const sampleWithPartialData: IOrgUnit = {
  title: 'ساختن',
  id: '119f4d9b-4fd9-46d4-8522-e02ff2ae11bf',
};

export const sampleWithFullData: IOrgUnit = {
  title: 'ناقص',
  id: '4a62db4b-b53b-4886-be9a-56010b412b87',
};

export const sampleWithNewData: NewOrgUnit = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
