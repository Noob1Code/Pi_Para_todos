import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './authService';

export const AuthInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>, 
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  const authService = inject(AuthService);
  const authHeader = authService.getAuthHeader();

  let request = req;
  if (authHeader && req.url.includes('http://localhost:8080/api/')) {
    request = req.clone({
      headers: req.headers.set('Authorization', authHeader),
    });
  }

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.warn('Interceptado erro 401, deslogando...');
        authService.logout();
      }
      return throwError(() => error);
    })
  );
};