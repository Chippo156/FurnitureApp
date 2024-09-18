import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
})
export class ForgotPasswordComponent {
  //   @ViewChild('forgotPasswordForm') forgotPasswordForm!: NgForm;
  forgotPasswordForm!: FormGroup;

  phoneNumber: string = '';
  password: string = '';
  password_confirm: string = '';

  constructor(private userService: UserService, private fb: FormBuilder) {}
  ngOnInit(): void {
    this.forgotPasswordForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      password_confirm: ['', [Validators.required]],
    });
  }

  resetPassword() {
    this.userService.updatePassword(this.phoneNumber, this.password).subscribe({
      next: (response) => {
        alert('Update password success');
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
