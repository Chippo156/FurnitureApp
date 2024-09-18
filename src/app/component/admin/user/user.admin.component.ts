import { Component, OnInit } from '@angular/core';
import { UserResponse } from 'src/app/responses/userResponse';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-admin',
  templateUrl: './user.admin.component.html',
  styleUrls: ['./user.admin.component.scss'],
})
export class UserAdminComponent implements OnInit {
  constructor(private userService: UserService) {}
  users: UserResponse[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 10;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  ngOnInit(): void {
    debugger;
    this.getUsers(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getUsers(keyword: string, page: number, limit: number) {
    debugger;
    this.userService.getUsers(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.users = response.userResponses;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePages(
          this.currentPage,
          this.totalPages
        );
      },
      complete: () => {},
      error: (error) => {
        debugger;
        console.error(error);
      },
    });
  }

  generateVisiblePages(currentPage: number, totalPages: number): number[] {
    const maxVisiblePage = 5;
    const haftVisiblePage = Math.floor(maxVisiblePage / 2);
    let startPage = Math.max(currentPage - haftVisiblePage, 1);
    let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages);
    if (endPage - startPage + 1 < maxVisiblePage) {
      startPage = Math.max(endPage - maxVisiblePage + 1, 1);
    }
    return new Array(endPage - startPage + 1)
      .fill(0)
      .map((_, index) => startPage + index);
  }
  onPageChange(page: number) {
    debugger;
    window.scrollTo(0, 0);
    this.currentPage = page;
    this.getUsers(this.keyword, this.currentPage - 1, this.itemsPerPage);
  }
  searchUser() {
    this.getUsers(this.keyword, this.currentPage, this.itemsPerPage);
  }
  updateUser(user: UserResponse) {
    debugger;
    this.userService.updateUser(user.id, user).subscribe({
      next: (response: any) => {
        debugger;
        alert('Update user successfully');
        this.getUsers(this.keyword, this.currentPage, this.itemsPerPage);
      },
      complete: () => {},
      error: (error) => {
        debugger;
        console.error(error);
      },
    });
  }
  RemoveUser(id: number) {
    this.userService.removeUser(id).subscribe({
      next: (response: any) => {
        debugger;
        this.getUsers(this.keyword, this.currentPage, this.itemsPerPage);
      },
      complete: () => {},
      error: (error) => {
        debugger;
        console.error(error);
      },
    });
  }
}
