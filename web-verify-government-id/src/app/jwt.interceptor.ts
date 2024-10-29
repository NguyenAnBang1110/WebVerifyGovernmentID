// jwt.interceptor.ts

import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { Router } from '@angular/router';
import { inject } from '@angular/core';
import { API_URL } from './constant';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

// Define JwtInterceptor as a functional interceptor
export const JwtInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const http = inject(HttpClient);
  const router = inject(Router);

  // Endpoints that do not require JWT
  const nonAuthUrls = [
    '/api/auth/register', 
    '/api/auth/login', 
    '/api/auth/refresh'
  ];

  // Skip JWT addition for non-auth URLs
  if (nonAuthUrls.some(url => req.url.includes(url))) {
    return next(req);
  }

  const token = localStorage.getItem('userToken');

  if (token && isTokenExpired(token)) {
    return handleTokenRefresh(req, next, http, router);
  }

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/auth/refresh')) {
        return handleTokenRefresh(req, next, http, router);
      }
      return throwError(() => error);
    })
  );
};

// Helper function to check if token is expired
const isTokenExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiry = payload.exp * 1000;
    return Date.now() > expiry;
  } catch (e) {
    console.error('Invalid token format', e);
    return true;
  }
};

// Helper function to handle token refresh
const handleTokenRefresh = (
  req: HttpRequest<any>,
  next: HttpHandlerFn,
  http: HttpClient,
  router: Router
): Observable<HttpEvent<any>> => {
  if (isRefreshing) {
    return refreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap((newToken) => {
        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${newToken}`
          }
        });
        return next(req);
      })
    );
  } else {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    const refreshToken = localStorage.getItem('refreshToken');

    return http.post(`${API_URL}/auth/refresh`, { refreshToken }).pipe(
      switchMap((response: any) => {
        const newToken = response.token;
        localStorage.setItem('userToken', newToken);
        isRefreshing = false;
        refreshTokenSubject.next(newToken);

        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${newToken}`
          }
        });

        return next(req);
      }),
      catchError((error) => {
        isRefreshing = false;
        refreshTokenSubject.next(null);
        localStorage.removeItem('userToken');
        localStorage.removeItem('refreshToken');
        router.navigate(['/login']);
        return throwError(() => new Error('Session expired, please login again'));
      })
    );
  }
};
