import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { UserService } from './service/user.service';
import { UserResponse } from './responses/userResponse';
import { HeaderComponent } from './component/header/header.component';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  @ViewChild(HeaderComponent) headerComponent!: HeaderComponent; // Reference to HeaderComponent

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService
  ) {}
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const paymentStatus = params['vnp_ResponseCode'];
      if (paymentStatus === '00') {
        alert('Thanh toán thành công');
      } else if (paymentStatus === '01') {
        alert('Thanh toán thất bại');
      }
      const token = params['access_token'];
      const providerId = params['providerId'];
      if (token) {
        localStorage.setItem('providerId', providerId);
        this.authService.login(token);
        this.router.navigate(['/']);
        // Save the token in local storage or a service
        // Redirect to a different page if needed
      }
    });
  }
  navigateHome() {
    this.router.navigate(['/'], { queryParams: { paymentStatus: '00' } });
  }
}
