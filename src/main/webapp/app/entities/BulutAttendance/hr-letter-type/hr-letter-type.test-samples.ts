import { IHrLetterType, NewHrLetterType } from './hr-letter-type.model';

export const sampleWithRequiredData: IHrLetterType = {
  id: '9325528c-61f9-47d3-8b8a-00724049efe3',
};

export const sampleWithPartialData: IHrLetterType = {
  id: '2d9d5b82-d2d0-4be9-a333-70c5d2f236e6',
};

export const sampleWithFullData: IHrLetterType = {
  title: 'والیبال',
  id: 'f4ce1410-21e7-4942-8a47-84611bb0662f',
};

export const sampleWithNewData: NewHrLetterType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
