import { Component, Input, OnInit } from '@angular/core';
import { AlertService } from '../service/alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
})
export class AlertComponent implements OnInit {
  title: string = 'Alert';
  message: string = 'This is an alert message';
  show: boolean = false;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.alertService.getAlert().subscribe((alert) => {
      this.title = alert.title;
      this.message = alert.message;
      this.show = true;
      setTimeout(() => {
        this.show = false;
      }, 3000); // Tự động ẩn sau 3 giây
    });
  }
  closeAlert(): void {
    this.show = false;
  }
}
