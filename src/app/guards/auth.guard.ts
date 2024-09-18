import { Injectable, inject } from '@angular/core';
import { TokenService } from '../service/token.service';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private tokenService: TokenService, private router: Router) {}
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const isTokenExpired = this.tokenService.isTokenExpired();
    const isUserIdValid = this.tokenService.getUserIdByToken() > 0;
    debugger;
    if (!isTokenExpired && isUserIdValid) {
      return true;
    } else {
      alert('Please login to continue');
      this.router.navigate(['/login']);
      return false;
    }
  }
  AuthGuarnFn: CanActivateFn = (
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean => {
    debugger;
    return inject(AuthGuard).canActivate(next, state);
  };
}
