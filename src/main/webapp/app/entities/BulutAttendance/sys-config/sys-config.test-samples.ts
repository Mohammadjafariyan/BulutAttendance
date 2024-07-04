import { ISysConfig, NewSysConfig } from './sys-config.model';

export const sampleWithRequiredData: ISysConfig = {
  id: '371675aa-9e3b-44d9-b91a-622f2d3c812a',
};

export const sampleWithPartialData: ISysConfig = {
  sanavatFormula: 'generally درک کردن',
  id: '67d29629-844c-41fd-839f-4c9fde49bd3c',
};

export const sampleWithFullData: ISysConfig = {
  taxFormula: 'واو همچون',
  sanavatFormula: 'همچنین اَخ هورا',
  year: 32749,
  id: 'caada639-e3ac-44d6-ad6b-fdafd0315522',
};

export const sampleWithNewData: NewSysConfig = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
