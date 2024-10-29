import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, @Inject(PLATFORM_ID) private platformId: Object) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // Chỉ sử dụng localStorage nếu đang chạy trên trình duyệt
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('userToken');

      if (token && !this.isTokenExpired(token)) {
        return true;
      } else {
        this.router.navigate(['/no-permission']);
        return false;
      }
    }

    // Trên server, chặn truy cập vì không có `localStorage`
    return false;
  }

  private isTokenExpired(token: string): boolean {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expiry = payload.exp * 1000;
    return Date.now() > expiry;
  }
}
