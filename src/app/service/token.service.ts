import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly TOKEN_KEY = 'access_token';
  private jwtHelper: JwtHelperService = new JwtHelperService();
  constructor() {}

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
  getUserIdByToken() {
    debugger;
    let userObject = this.jwtHelper.decodeToken(this.getToken() ?? '');
    if (userObject === null) return 0;
    return 'userId' in userObject ? parseInt(userObject['userId']) : 0;
  }
  isTokenExpired(): boolean {
    if (this.getToken() === null) return false;
    return this.jwtHelper.isTokenExpired(this.getToken()!);
  }
}
