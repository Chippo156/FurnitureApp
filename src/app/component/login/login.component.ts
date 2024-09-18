import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../../dtos/loginDto';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { LoginResponse } from '../../responses/LoginResponse';
import { TokenService } from '../../service/token.service';
import { NgForm } from '@angular/forms';
import { UserResponse } from '../../responses/userResponse';
import { initFlowbite } from 'flowbite';
import { AlertService } from 'src/app/service/alert.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  phone_number: string;
  password: string;
  rememberMe: boolean = true;
  userResponse?: UserResponse;

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private alertService: AlertService,
    private oauthService: OAuthService,
    private authService: AuthService
  ) {
    this.phone_number = '';
    this.password = '';
  }
  login() {
    const message = `Phone number: ${this.phone_number}, Password: ${this.password}`;
    const loginDto: LoginDTO = {
      phone_number: this.phone_number,
      password: this.password,
    };
    this.userService.login(loginDto).subscribe({
      next: (response: LoginResponse) => {
        debugger;
        const { token } = response;
        this.tokenService.setToken(token);
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
            if (this.userResponse.role.name === 'ADMIN') {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/']);
            }
          },
          complete: () => {
            debugger;
          },
          error: (error) => {
            debugger;
            this.alertService.showAlert('error', error.error);
          },
        });
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        debugger;
        this.alertService.showAlert('error', error.error);
      },
    });
  }
  redirectToGoogleOAuth() {
    debugger;
    this.authService.googleOAuth('');
  }
  redirectToFacebookOAuth() {
    debugger;
    this.authService.facebookOAuth('');
  }
  redirectToGithubOAuth() {
    debugger;
    this.authService.githubOAuth();
  }
}
