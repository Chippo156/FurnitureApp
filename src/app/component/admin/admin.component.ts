import { Component, Injectable, Input, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { TokenService } from '../../service/token.service';
import { Router } from '@angular/router';
import { UserResponse } from 'src/app/responses/userResponse';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss'],
})
export class AdminComponent implements OnInit {
  userResponse?: UserResponse;
  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
    const currentAdminPage = localStorage.getItem('currentAdminPage');
    if (typeof currentAdminPage === 'string' && currentAdminPage !== null) {
      this.router.navigate([currentAdminPage]);
    }
  }
  showDashBoard(adminComponent: string) {}
  logout() {
    this.userService.removeUserResponseFromLocalStorage();
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }
  showAdminComponent(componentName: string) {
    this.router.navigate([`/admin/${componentName}`]);
    localStorage.setItem('currentAdminPage', `/admin/${componentName}`);
  }
}
