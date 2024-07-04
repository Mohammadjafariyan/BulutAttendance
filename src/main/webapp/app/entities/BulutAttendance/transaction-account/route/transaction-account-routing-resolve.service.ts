import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionAccount } from '../transaction-account.model';
import { TransactionAccountService } from '../service/transaction-account.service';

const transactionAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransactionAccount> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransactionAccountService)
      .find(id)
      .pipe(
        mergeMap((transactionAccount: HttpResponse<ITransactionAccount>) => {
          if (transactionAccount.body) {
            return of(transactionAccount.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default transactionAccountResolve;
