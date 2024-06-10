import { inject, NgModule } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateService, TranslateLoader, MissingTranslationHandler } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { translatePartialLoader, missingTranslationHandler } from 'app/config/translation.config';
import { StateStorageService } from 'app/core/auth/state-storage.service';

function lazyTranslatePartialLoader(http: HttpClient): TranslateLoader {
  return new TranslateHttpLoader(http, 'services/bulutattendance/i18n/', `.json?_=${I18N_HASH}`);
}

@NgModule({
  imports: [
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: lazyTranslatePartialLoader,
        deps: [HttpClient],
      },
      isolate: false,
      extend: true,
    }),
  ],
})
export class LazyTranslationModule {
  private translateService = inject(TranslateService);
  private translateLoader = inject(TranslateLoader);
  private stateStorageService = inject(StateStorageService);

  constructor() {
    const currentLang = this.translateService.store.currentLang;
    this.translateLoader.getTranslation(currentLang).subscribe(translation => {
      this.translateService.setTranslation(currentLang, translation);
    });
  }
}

@NgModule({
  imports: [
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: translatePartialLoader,
        deps: [HttpClient],
      },
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useFactory: missingTranslationHandler,
      },
    }),
  ],
})
export class TranslationModule {
  private translateService = inject(TranslateService);
  private stateStorageService = inject(StateStorageService);

  constructor() {
    this.translateService.setDefaultLang('fa');
    // if user have changed language and navigates away from the application and back to the application then use previously choosed language
    const langKey = this.stateStorageService.getLocale() ?? 'fa';
    this.translateService.use(langKey);
  }
}
