// authService.ts
import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environtment } from '../environments/environment';
import { AppContants } from '../common/AppContants';
import { BehaviorSubject } from 'rxjs';
import { UserService } from './user.service';
import { UserResponse } from '../responses/userResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  userResponse: UserResponse | undefined;
  constructor(
    private oauthService: OAuthService,
    private http: HttpClient,
    private userService: UserService
  ) {}
  googleOAuth(phoneNumber: string) {
    if (phoneNumber !== '') window.location.href = `${AppContants.URL_GOOGLE}`;
    else window.location.href = `${AppContants.URL_GOOGLE}`;
  }
  facebookOAuth(phoneNumber: string) {
    if (phoneNumber !== '')
      window.location.href = `${AppContants.URL_FACEBOOK}`;
    else window.location.href = `${AppContants.URL_FACEBOOK}`;
  }
  githubOAuth() {
    window.location.href = `${AppContants.URL_GITHUB}`;
  }

  private loggedIn = new BehaviorSubject<boolean>(false);

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  login(token: string) {
    localStorage.setItem('access_token', token);
    this.userService.getUserDetails(token).subscribe({
      next: (response: any) => {
        debugger;
        this.userResponse = {
          id: response.id,
          full_name: response.full_name,
          phone_number: response.phone_number,
          address: response.address,
          is_active: response.is_active,
          date_of_birth: new Date(response.date_of_birth),
          facebook_account_id: response.facebook_account_id,
          google_account_id: response.google_account_id,
          role: response.role,
        };
        this.userService.saveUserResponseToLocalStorage(this.userResponse);
      },
    });
    this.loggedIn.next(true);
  }

  logout() {
    this.userService.removeUserResponseFromLocalStorage();
    localStorage.removeItem('access_token');
    this.loggedIn.next(false);
  }
}
