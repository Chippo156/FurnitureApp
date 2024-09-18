import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserResponse } from 'src/app/responses/userResponse';
import { AlertService } from 'src/app/service/alert.service';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  @ViewChild('changePassword') changePassword!: NgForm;

  user!: UserResponse;
  dateOfBirth!: string;
  checked = true;
  checkEdit = false;
  newPassword: string = '';
  confirmPassword: string = '';
  ngOnInit(): void {
    this.user = this.userService.getUserResponseFromLocalStorage()!;
    this.user.date_of_birth = new Date(this.user.date_of_birth);
    const dob = this.user.date_of_birth;
    const formattedDate =
      dob.getFullYear() +
      '-' +
      ('0' + (dob.getMonth() + 1)).slice(-2) +
      '-' +
      ('0' + dob.getDate()).slice(-2);

    this.dateOfBirth = formattedDate;
  }
  constructor(
    private userService: UserService,
    private authService: AuthService
  ) {}
  Edit() {
    this.checkEdit = true;
    let input = document.querySelectorAll('input');
    for (let i = 0; i < input.length; i++) {
      input[i].removeAttribute('disabled');
    }
  }
  updateUser() {
    debugger;
    const check = window.confirm(
      'Are you sure you want to update your profile?'
    );
    if (check && this.checkEdit) {
      this.userService.updateUser(this.user.id, this.user).subscribe({
        next: (response) => {
          this.userService.saveUserResponseToLocalStorage(this.user);
        },
        complete: () => {
          this.checkEdit = false;
          let input = document.querySelectorAll('input');
          for (let i = 0; i < input.length; i++) {
            input[i].setAttribute('disabled', 'true');
          }
        },
        error: (error) => {
          console.log(error);
        },
      });
    }
  }
  change(item: string) {
    if (item === 'profile') {
      this.checked = !this.checked;
    } else {
      this.checked = false;
    }
  }
  updatePassword() {
    const check = window.confirm(
      'Are you sure you want to update your password?'
    );
    if (!check && !this.checkEdit) {
      return;
    }
    this.userService
      .updatePassword(this.user.phone_number, this.newPassword)
      .subscribe({
        next: (response) => {
          alert(response);
        },
        complete: () => {
          this.checkEdit = false;
          let input = document.querySelectorAll('input');
          for (let i = 0; i < input.length; i++) {
            input[i].setAttribute('disabled', 'true');
          }
        },
        error: (error) => {
          console.log(error);
        },
      });
  }
  checkConfirmPassword() {
    if (this.newPassword !== this.confirmPassword) {
      this.changePassword.form.controls['confirmPassword'].setErrors({
        invalidPassword: true,
      });
    } else {
      this.changePassword.form.controls['confirmPassword'].setErrors(null);
    }
  }
  updateFacebookAccount() {
    this.authService.facebookOAuth(this.user.phone_number);
  }
  updateGoogleAccount() {
    this.authService.googleOAuth(this.user.phone_number);
  }
}
