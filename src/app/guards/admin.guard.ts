import { Injectable, inject } from '@angular/core';
import { TokenService } from '../service/token.service';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { UserService } from '../service/user.service';
import { UserResponse } from '../responses/userResponse';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard {
  userRespose!: UserResponse | undefined;
  constructor(
    private tokenService: TokenService,
    private router: Router,
    private userService: UserService
  ) {}
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const isTokenExpired = this.tokenService.isTokenExpired();
    const isUserIdValid = this.tokenService.getUserIdByToken() > 0;
    this.userRespose = this.userService.getUserResponseFromLocalStorage();
    const isAdmin = this.userRespose?.role.name === 'ADMIN';
    debugger;
    if (!isTokenExpired && isUserIdValid) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
  AdminGuarnFn: CanActivateFn = (
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean => {
    debugger;
    return inject(AdminGuard).canActivate(next, state);
  };
}
