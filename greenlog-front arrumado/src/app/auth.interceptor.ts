import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './authService';

export const AuthInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>, 
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  const authService = inject(AuthService);
  const authHeader = authService.getAuthHeader();

  if (authHeader && req.url.includes('http://localhost:8080/api/')) {
    const clonedRequest = req.clone({
      headers: req.headers.set('Authorization', authHeader),
    });
    return next(clonedRequest);
  }

  return next(req);
};
