import { IPersonnel, NewPersonnel } from './personnel.model';

export const sampleWithRequiredData: IPersonnel = {
  id: 'fd3e9360-05ff-4707-b394-583ff2c76fd8',
};

export const sampleWithPartialData: IPersonnel = {
  requitmentDate: 'لیکن باور نکردن پرنده',
  father: 'تسلط داشتن همچنین',
  birthday: 'دانشگاه',
  isSingle: 'آه همچنین دشوار',
  lastEducation: 'درباره gently',
  educationField: 'عینک sadly برابر با',
  id: '842dfa33-0480-4bd9-a510-543ce0f8af81',
};

export const sampleWithFullData: IPersonnel = {
  firstName: 'سپیدار',
  lastName: 'عبادی',
  requitmentDate: 'daintily قبول کردن',
  father: 'یا به نوعی',
  shenasname: 'و واو',
  mahalesodur: 'ورزش ناشناخته',
  birthday: 'با توجه به',
  isSingle: 'یا با',
  lastEducation: 'کند باور نکردن دیدن',
  educationField: 'تا اوه',
  children: 42,
  id: 'eab73c92-102b-44a7-84ac-f2ebdeee32f6',
};

export const sampleWithNewData: NewPersonnel = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
