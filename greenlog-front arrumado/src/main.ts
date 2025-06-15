import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient } from '@angular/common/http';
import { LOCALE_ID } from '@angular/core';
import { appConfig } from './app/app.config';
import { registerLocaleData } from '@angular/common';
import { provideEchartsCore } from 'ngx-echarts';
import localePt from '@angular/common/locales/pt';
registerLocaleData(localePt);

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    ...(appConfig.providers || []),
    provideHttpClient(),
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    provideEchartsCore({
      echarts: () => import('echarts')
    })
  ]
}).catch(err => console.error(err));
